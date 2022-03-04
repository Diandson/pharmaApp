import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';

import {DATE_TIME_FORMAT} from 'app/config/input.constants';

import {Depense, IDepense} from '../depense.model';
import {DepenseService} from '../service/depense.service';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ProgressDialogComponent} from "../../../shared/progress-dialog/progress-dialog.component";
import {NzModalService} from "ng-zorro-antd/modal";

@Component({
  selector: 'jhi-depense-update',
  templateUrl: './depense-update.component.html',
  styleUrls: ['../depense.component.scss']
})
export class DepenseUpdateComponent implements OnInit {
  isSaving = false;
  depense?: IDepense;

  editForm = this.fb.group({
    id: [],
    numero: [],
    motifDepense: [],
    ordonnateur: [],
    justificatif: [],
    montant: [],
    dateDepense: [],
  });

  constructor(
    protected depenseService: DepenseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private modal: NzModalService
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ depense }) => {
    //   if (depense.id === undefined) {
    //     depense.dateDepense = dayjs().startOf('day');
    //   }
    //
    //   this.updateForm(depense);
    // });
    if (this.depense){
      this.updateForm(this.depense)
    }

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
    const depense = this.createFromForm();

    if (depense.id) {
      this.depenseService.update(depense).subscribe(res => {
        if (res.body){
          modalRef.close();
          this.activeModal.close('succes')
          this.success('Dépense mis à jour avec succès!');
        }else {
          modalRef.close();
          this.warning('Erreur non définie!')
        }
      }, () => {
        modalRef.close();
        this.error('Erreur serveur non joingnable!')
      });
    } else {
      this.depenseService.create(depense).subscribe(res => {
        if (res.body){
          modalRef.close();
          this.activeModal.close('succes')
          this.success('Dépense ajouté avec succès!');
        }else {
          modalRef.close();
          this.warning('Erreur non définie!')
        }
      }, () => {
        modalRef.close();
        this.error('Erreur serveur non joingnable!')
      });
    }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepense>>): void {
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

  protected updateForm(depense: IDepense): void {
    this.editForm.patchValue({
      id: depense.id,
      numero: depense.numero,
      motifDepense: depense.motifDepense,
      ordonnateur: depense.ordonnateur,
      justificatif: depense.justificatif,
      montant: depense.montant,
      dateDepense: depense.dateDepense ? depense.dateDepense.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IDepense {
    return {
      ...new Depense(),
      id: this.editForm.get(['id'])!.value,
      motifDepense: this.editForm.get(['motifDepense'])!.value,
      ordonnateur: this.editForm.get(['ordonnateur'])!.value,
      justificatif: this.editForm.get(['justificatif'])!.value,
      montant: this.editForm.get(['montant'])!.value
    };
  }
}
