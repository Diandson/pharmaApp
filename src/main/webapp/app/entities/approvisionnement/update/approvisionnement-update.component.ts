import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IApprovisionnement, Approvisionnement } from '../approvisionnement.model';
import { ApprovisionnementService } from '../service/approvisionnement.service';

@Component({
  selector: 'jhi-approvisionnement-update',
  templateUrl: './approvisionnement-update.component.html',
})
export class ApprovisionnementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [],
    agenceApp: [],
    commentaire: [],
    dateCommande: [],
  });

  constructor(
    protected approvisionnementService: ApprovisionnementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvisionnement }) => {
      if (approvisionnement.id === undefined) {
        const today = dayjs().startOf('day');
        approvisionnement.dateCommande = today;
      }

      this.updateForm(approvisionnement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvisionnement = this.createFromForm();
    if (approvisionnement.id !== undefined) {
      this.subscribeToSaveResponse(this.approvisionnementService.update(approvisionnement));
    } else {
      this.subscribeToSaveResponse(this.approvisionnementService.create(approvisionnement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovisionnement>>): void {
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

  protected updateForm(approvisionnement: IApprovisionnement): void {
    this.editForm.patchValue({
      id: approvisionnement.id,
      numero: approvisionnement.numero,
      agenceApp: approvisionnement.agenceApp,
      commentaire: approvisionnement.commentaire,
      dateCommande: approvisionnement.dateCommande ? approvisionnement.dateCommande.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IApprovisionnement {
    return {
      ...new Approvisionnement(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      agenceApp: this.editForm.get(['agenceApp'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      dateCommande: this.editForm.get(['dateCommande'])!.value
        ? dayjs(this.editForm.get(['dateCommande'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
