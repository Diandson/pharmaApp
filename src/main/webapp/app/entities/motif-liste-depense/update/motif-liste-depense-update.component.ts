import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMotifListeDepense, MotifListeDepense } from '../motif-liste-depense.model';
import { MotifListeDepenseService } from '../service/motif-liste-depense.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-motif-liste-depense-update',
  templateUrl: './motif-liste-depense-update.component.html',
})
export class MotifListeDepenseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    montant: [],
  });

  constructor(
    protected motifListeDepenseService: MotifListeDepenseService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motifListeDepense }) => {
      this.updateForm(motifListeDepense);
    });
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    const motifListeDepense = this.createFromForm();
    if (motifListeDepense.id) {
      this.subscribeToSaveResponse(this.motifListeDepenseService.update(motifListeDepense));
    } else {
      this.subscribeToSaveResponse(this.motifListeDepenseService.create(motifListeDepense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMotifListeDepense>>): void {
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

  protected updateForm(motifListeDepense: IMotifListeDepense): void {
    this.editForm.patchValue({
      id: motifListeDepense.id,
      libelle: motifListeDepense.libelle,
      montant: motifListeDepense.montant,
    });
  }

  protected createFromForm(): IMotifListeDepense {
    return {
      ...new MotifListeDepense(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      montant: this.editForm.get(['montant'])!.value,
    };
  }
}
