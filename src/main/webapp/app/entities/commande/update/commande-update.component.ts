import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import {DATE_TIME_FORMAT} from 'app/config/input.constants';

import {Commande, ICommande} from '../commande.model';
import {CommandeService} from '../service/commande.service';
import {LivraisonService} from 'app/entities/livraison/service/livraison.service';
import {FournisseurService} from 'app/entities/fournisseur/service/fournisseur.service';
import {PersonneService} from 'app/entities/personne/service/personne.service';
import { NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MedicamentService} from "../../medicament/service/medicament.service";
import {IMedicament} from "../../medicament/medicament.model";
import {createMask} from "@ngneat/input-mask";
import {NzModalService} from "ng-zorro-antd/modal";
import { ICommandeMedicament} from "../../commande-medicament/commande-medicament.model";
import {ProgressDialogComponent} from "../../../shared/progress-dialog/progress-dialog.component";
import {Fournisseur, IFournisseur} from "../../fournisseur/fournisseur.model";
import {GenererComponent} from "../generer/generer.component";

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
  styleUrls: ['../commande.component.scss']
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;
  commande: ICommande = new Commande()
  commandeMedicament: ICommandeMedicament[] = [];
  medicaments?: IMedicament[];
  medicamentList: IMedicament[] = [];
  fournisseurs?: IFournisseur[];

  dateInputMask = createMask<Date>({
    alias: 'datetime',
    inputFormat: 'dd/mm/yyyy',
    parser: (value: string) => {
      const values = value.split('/');
      const year = +values[2];
      const month = +values[1] - 1;
      const date = +values[0];
      return new Date(year, month, date);
    },
  });

  editForm = this.fb.group({
    id: [],
    numero: [],
    dateCommande: [],
    livraison: [],
    fournisseur: [],
    operateur: [],
  });
  search?: string;
  prixTotal = 0;
  isVisible = false;
  selectedFournisseur: IFournisseur = new Fournisseur();

  constructor(
    protected commandeService: CommandeService,
    protected livraisonService: LivraisonService,
    protected fournisseurService: FournisseurService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected medicamentService: MedicamentService,
    private modalService: NgbModal,
    private modal: NzModalService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ commande }) => {
    //   if (commande.id === undefined) {
    //     commande.dateCommande = dayjs().startOf('day');
    //   }
    // });

    this.medicamentService.query()
      .subscribe( res => {
        this.medicaments = res.body ?? [];
        this.medicaments.forEach(me => {
          me.stockTheorique = 0;
        })
      })

    this.fournisseurService.query()
      .subscribe(res => this.fournisseurs = res.body ?? []);
  }

  previousState(): void {
    window.history.back();
    // this.activeModal.close()
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  saveImprime(): void {
    const modalRef = this.modalService.open(ProgressDialogComponent, {
      backdrop: 'static',
      centered: true,
      windowClass: 'myCustomModalClass',
    });

    const commande = new Commande();
    commande.medicament = this.medicamentList;
    commande.fournisseur = this.selectedFournisseur;

    this.commandeService.createImprime(commande).subscribe(res => {
      if (res.size){
        modalRef.close();
        this.success('Commande créée avec succès!');
      }else {
        modalRef.close();
        this.warning('Erreur non définie!')
      }
    }, () => {
      modalRef.close();
      this.error('Erreur serveur non joingnable!')
    });
  }

  getPrixAchat(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }
  getPrixPublic(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }
  getStockTheorique(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }

  addMedicamentToCommande(medicament: IMedicament): any {
    if (medicament.stockTheorique! > 0) {
      const medicam = this.medicamentList.find(ex => ex.id === medicament.id);
      if (medicam) {
        this.warning('Ce médicament existe déjà sur la liste!');
      } else {
        this.medicamentList.push(medicament);
        this.medicamentList.sort().reverse();

        this.prixTotal = this.prixTotal + medicament.prixPublic! * medicament.stockTheorique!;
      }
    } else {
      this.warning('Le stock de ce médicament est epuisé veuillez vous reapprovisionner!');
    }
  }
  removeMedicament(medi: IMedicament): void {
    this.modal.warning({
      nzContent: 'Voulez-vous vraiment retirer ce produit?',
      nzTitle: 'ATTENTION',
      nzOkText: 'Oui',
      nzCancelText: 'Non',
      nzOnOk: () => {
        this.prixTotal = 0;
        const index = this.medicamentList.findIndex(value => value.id === medi.id);
        this.medicamentList.splice(index, 1);
        this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.prixPublic! * value.stockTheorique!));
      },
    });
  }

  showModal(): void {
    this.isVisible = true;
  }
  handleCancel(): void {
    this.isVisible = false;
  }

  genererCommande(): void {
    const modalRef = this.modalService.open(GenererComponent, { size: 'xl', backdrop: 'static' });
    modalRef.result.then((res) => {
      if (res !== 'close'){
        this.previousState();
      }
    })
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      numero: commande.numero,
      dateCommande: commande.dateCommande ? commande.dateCommande.format(DATE_TIME_FORMAT) : null,
      livraison: commande.livraison,
      fournisseur: commande.fournisseur,
      operateur: commande.operateur,
    });
  }

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateCommande: this.editForm.get(['dateCommande'])!.value
        ? dayjs(this.editForm.get(['dateCommande'])!.value, DATE_TIME_FORMAT)
        : undefined,
      livraison: this.editForm.get(['livraison'])!.value,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
