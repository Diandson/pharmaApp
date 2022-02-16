import { Component, OnDestroy, OnInit,} from '@angular/core';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';

import {AccountService} from 'app/core/auth/account.service';
import {Account} from 'app/core/auth/account.model';
import {MedicamentService} from "../entities/medicament/service/medicament.service";
import {IMedicament} from "../entities/medicament/medicament.model";
import {Authority} from "../config/authority.constants";
import {IVenteMedicament, VenteMedicament} from "../entities/vente-medicament/vente-medicament.model";

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
  medicamentList?: IMedicament[];
  private readonly destroy$ = new Subject<void>();

  constructor(
    protected medicamentService: MedicamentService,
    private accountService: AccountService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState()
      .subscribe(account => (this.account = account));

    if (this.accountService.hasAnyAuthority([Authority.SERVEUR, Authority.STRUCTURE_ADMIN])){
      this.medicamentService.query()
        .subscribe( res => {
          this.medicaments = res.body ?? []
        })
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  isAuthenticated(): boolean{
    return this.accountService.isAuthenticated();
  }

  getSelectedMedicament($event: IMedicament): void {
    const existMedicament = this.medicamentList?.find(m => m.dci === $event.dci);
    if (existMedicament){
      existMedicament.stockTheorique = existMedicament.stockTheorique! + 1
    }else {
      $event.stockTheorique = 1;
      const vm = new VenteMedicament();
      vm.medicament = $event;
      // vm.montant = String($event.prixPublic);
      vm.quantite = $event.stockTheorique;

      this.medicamentList?.push($event);
      this.medicamentVendu?.push(vm);

      alert(this.medicamentList?.length);
    }
  }
}
