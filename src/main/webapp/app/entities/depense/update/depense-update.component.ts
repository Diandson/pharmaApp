import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDepense, Depense } from '../depense.model';
import { DepenseService } from '../service/depense.service';

@Component({
  selector: 'jhi-depense-update',
  templateUrl: './depense-update.component.html',
})
export class DepenseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [],
    motifDepense: [],
    ordonnateur: [],
    justificatif: [],
    montant: [],
    dateDepense: [],
  });

  constructor(protected depenseService: DepenseService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depense }) => {
      if (depense.id === undefined) {
        const today = dayjs().startOf('day');
        depense.dateDepense = today;
      }

      this.updateForm(depense);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depense = this.createFromForm();
    if (depense.id !== undefined) {
      this.subscribeToSaveResponse(this.depenseService.update(depense));
    } else {
      this.subscribeToSaveResponse(this.depenseService.create(depense));
    }
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
      numero: this.editForm.get(['numero'])!.value,
      motifDepense: this.editForm.get(['motifDepense'])!.value,
      ordonnateur: this.editForm.get(['ordonnateur'])!.value,
      justificatif: this.editForm.get(['justificatif'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      dateDepense: this.editForm.get(['dateDepense'])!.value
        ? dayjs(this.editForm.get(['dateDepense'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
