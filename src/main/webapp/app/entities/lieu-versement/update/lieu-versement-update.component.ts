import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILieuVersement, LieuVersement } from '../lieu-versement.model';
import { LieuVersementService } from '../service/lieu-versement.service';

@Component({
  selector: 'jhi-lieu-versement-update',
  templateUrl: './lieu-versement-update.component.html',
})
export class LieuVersementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
  });

  constructor(protected lieuVersementService: LieuVersementService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lieuVersement }) => {
      this.updateForm(lieuVersement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lieuVersement = this.createFromForm();
    if (lieuVersement.id !== undefined) {
      this.subscribeToSaveResponse(this.lieuVersementService.update(lieuVersement));
    } else {
      this.subscribeToSaveResponse(this.lieuVersementService.create(lieuVersement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILieuVersement>>): void {
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

  protected updateForm(lieuVersement: ILieuVersement): void {
    this.editForm.patchValue({
      id: lieuVersement.id,
      libelle: lieuVersement.libelle,
    });
  }

  protected createFromForm(): ILieuVersement {
    return {
      ...new LieuVersement(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
    };
  }
}
