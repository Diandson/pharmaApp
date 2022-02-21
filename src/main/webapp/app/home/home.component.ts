import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable, of, startWith, Subject, Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { MedicamentService } from '../entities/medicament/service/medicament.service';
import { IMedicament } from '../entities/medicament/medicament.model';
import { Authority } from '../config/authority.constants';
import { IVenteMedicament } from '../entities/vente-medicament/vente-medicament.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NzModalService } from 'ng-zorro-antd/modal';
import { ProgressDialogComponent } from '../shared/progress-dialog/progress-dialog.component';
import { Vente } from '../entities/vente/vente.model';
import { VenteService } from '../entities/vente/service/vente.service';
import { VenteSocketService } from './vente-socket.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  medicaments?: IMedicament[];
  medicamentSelect?: IMedicament;
  medicamentVendu?: IVenteMedicament[];
  medicamentList: IMedicament[] = [];
  filteredMedicament?: Observable<IMedicament[]>;

  @ViewChild('input') input: any;
  prixTotal = 0;

  ventes: Vente[] = [];
  subscription?: Subscription;

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected medicamentService: MedicamentService,
    private accountService: AccountService,
    private modalService: NgbModal,
    private modal: NzModalService,
    private venteService: VenteService,
    private venteSocketService: VenteSocketService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    if (this.accountService.hasAnyAuthority([Authority.SERVEUR])) {
      this.medicamentService.query().subscribe(res => {
        this.medicaments = res.body ?? [];
        this.filteredMedicament = of(this.medicaments);
      });
    } else if (this.accountService.hasAnyAuthority([Authority.STRUCTURE_CAISSE, Authority.USER])) {
      this.venteSocketService.subscribe();
      this.subscription = this.venteSocketService.receive().subscribe(res => {
        this.ventes = res;
        alert(this.ventes.length);
      });
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.venteSocketService.unsubscribe();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  getFilteredOptions(value: string): Observable<IMedicament[]> {
    return of(value).pipe(
      startWith(''),
      map(filterString => this.filter(filterString))
    );
  }
  onChange(): any {
    this.filteredMedicament = this.getFilteredOptions(this.input.nativeElement.value);
  }

  onSelectionChange($event: IMedicament): any {
    this.filteredMedicament = this.getFilteredOptions($event.dci!.toString());

    if ($event.stockTheorique! > 0) {
      const medicam = this.medicamentList.find(ex => ex.id === $event.id);
      if (medicam) {
        medicam.stockTheorique = medicam.stockTheorique! + 1;
        this.prixTotal = this.prixTotal + medicam.prixPublic! / (medicam.stockTheorique - 1);
        // medicam.prixPublic = (medicam.stockTheorique * $event.prixPublic!) / (medicam.stockTheorique - 1);
      } else {
        $event.stockTheorique = 1;
        this.medicamentList.push($event);
        this.medicamentList.sort().reverse();
        this.prixTotal = this.prixTotal + $event.prixPublic!;
      }
    } else {
      this.warning('Le stock de ce médicament est epuisé veuillez vous reapprovisionner!');
    }
    this.input.nativeElement.value = null;
  }

  success(msg: string): void {
    this.modal.success({
      nzContent: msg,
      nzTitle: 'SUCCESS',
      nzOkText: 'OK',
    });
  }
  warning(msg: string): void {
    this.modal.warning({
      nzContent: msg,
      nzTitle: 'ATTENTION',
      nzOkText: 'OK',
    });
  }
  error(msg: string): void {
    this.modal.error({
      nzContent: msg,
      nzTitle: 'ERROR',
      nzOkText: 'OK',
    });
  }

  detailMedic(medi: IMedicament): void {
    this.medicamentSelect = medi;
  }

  getQuantite(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }

  remove(medi: IMedicament): void {
    this.modal.warning({
      nzContent: 'Voulez-vous vraiment retirer ce produit?',
      nzTitle: 'ATTENTION',
      nzOkText: 'Oui',
      nzCancelText: 'Non',
      nzOnOk: () => {
        this.prixTotal = 0;
        const index = this.medicamentList.findIndex(value => value.id === medi.id);
        this.medicamentList.splice(index, 1);
        this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.prixPublic!));
      },
    });
  }

  validerVente(): void {
    const modalRef = this.modalService.open(ProgressDialogComponent, {
      backdrop: 'static',
      centered: true,
      windowClass: 'myCustomModalClass',
    });

    const vente = new Vente();
    vente.montant = this.prixTotal;
    vente.medicament = this.medicamentList;

    this.venteService.create(vente).subscribe(
      res => {
        if (res.body) {
          modalRef.close();
          this.success('Prescription efectuee avec succes!');
        } else {
          modalRef.close();
          this.warning('Une erreur est survenue!');
        }
      },
      () => {
        modalRef.close();
        this.error('Erreur serveur verifier votre connexion!');
      }
    );
  }

  private filter(value: string): IMedicament[] {
    const filterValue = value.toLowerCase();
    return this.medicaments!.filter(medi => medi.dci!.toLowerCase().lastIndexOf(filterValue) === 0);
  }
}
