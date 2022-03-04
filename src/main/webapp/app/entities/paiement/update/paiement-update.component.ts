import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';
import {DATE_TIME_FORMAT} from 'app/config/input.constants';

import {IPaiement, Paiement} from '../paiement.model';
import {PaiementService} from '../service/paiement.service';
import {VersementService} from 'app/entities/versement/service/versement.service';
import {PersonneService} from 'app/entities/personne/service/personne.service';
import {IVente} from 'app/entities/vente/vente.model';
import {VenteService} from 'app/entities/vente/service/vente.service';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {createMask} from "@ngneat/input-mask";
import {IMedicament} from "../../medicament/medicament.model";
import {NzModalService} from "ng-zorro-antd/modal";
import {ProgressDialogComponent} from "../../../shared/progress-dialog/progress-dialog.component";

@Component({
  selector: 'jhi-paiement-update',
  templateUrl: './paiement-update.component.html',
  styleUrls: ['../paiement.component.scss']
})
export class PaiementUpdateComponent implements OnInit {
  isSaving = false;
  ventesCollection: IVente[] = [];
  medicament: IMedicament[] = [];
  vente?: IVente;
  telephoneInputMask = createMask('99 999 999');
  avoir = 0;
  sommeDonner = 0;
  sommeRecu = 0;

  editForm = this.fb.group({
    id: [],
    numero: [],
    numeroVente: [],
    datePaiement: [],
    sommeRecu: [],
    sommeDonner: [],
    avoir: [],
    versement: [],
    operateur: [],
    vente: [],
  });


  constructor(
    protected paiementService: PaiementService,
    protected versementService: VersementService,
    protected personneService: PersonneService,
    protected venteService: VenteService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    private modal: NzModalService,
    private modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ paiement }) => {
    //   this.updateForm(paiement);
    // });
    this.medicament = this.vente!.medicament ?? [];
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    const modalRef = this.modalService.open(ProgressDialogComponent, {
      backdrop: 'static',
      centered: true,
      windowClass: 'myCustomModalClass',
    });
    const paiement = this.createFromForm();
    this.paiementService.create(paiement).subscribe(res => {
      if (res.body){
        modalRef.close();
        this.success('Paiement effectué avec succès!!');
        this.activeModal.close('succes')
      }else {
        modalRef.close();
        this.warning('Une erreur est survenue!')
      }
    }, () => {
      modalRef.close()
      this.error('Erreur serveur injoignable!')
    })
    // if (paiement.id !== undefined) {
    //   this.subscribeToSaveResponse(this.paiementService.update(paiement));
    // } else {
    //   this.subscribeToSaveResponse(this.paiementService.create(paiement));
    // }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiement>>): void {
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

  protected updateForm(paiement: IPaiement): void {
    this.editForm.patchValue({
      id: paiement.id,
      numero: paiement.numero,
      numeroVente: paiement.numeroVente,
      datePaiement: paiement.datePaiement ? paiement.datePaiement.format(DATE_TIME_FORMAT) : null,
      sommeRecu: paiement.sommeRecu,
      sommeDonner: paiement.sommeDonner,
      avoir: paiement.avoir,
      versement: paiement.versement,
      operateur: paiement.operateur,
      vente: paiement.vente,
    });
  }

  protected createFromForm(): IPaiement {
    return {
      ...new Paiement(),
      id: this.editForm.get(['id'])!.value,
      sommeRecu: this.editForm.get(['sommeRecu'])!.value,
      sommeDonner: this.editForm.get(['sommeDonner'])!.value,
      avoir: this.avoir,
      vente: this.vente,
    };
  }
}
