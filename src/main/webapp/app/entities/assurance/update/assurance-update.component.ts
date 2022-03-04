import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAssurance, Assurance } from '../assurance.model';
import { AssuranceService } from '../service/assurance.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-assurance-update',
  templateUrl: './assurance-update.component.html',
})
export class AssuranceUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required]],
    taux: [],
    email: [],
    operateur: [],
  });

  constructor(
    protected assuranceService: AssuranceService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assurance }) => {
      this.updateForm(assurance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    const assurance = this.createFromForm();
    if (assurance.id) {
      this.subscribeToSaveResponse(this.assuranceService.update(assurance));
    } else {
      this.subscribeToSaveResponse(this.assuranceService.create(assurance));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssurance>>): void {
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

  protected updateForm(assurance: IAssurance): void {
    this.editForm.patchValue({
      id: assurance.id,
      libelle: assurance.libelle,
      taux: assurance.taux,
      email: assurance.email,
      operateur: assurance.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      assurance.operateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing(personnes, this.editForm.get('operateur')!.value)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }

  protected createFromForm(): IAssurance {
    return {
      ...new Assurance(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      taux: this.editForm.get(['taux'])!.value,
      email: this.editForm.get(['email'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
