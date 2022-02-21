import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVersement, Versement } from '../versement.model';
import { VersementService } from '../service/versement.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-versement-update',
  templateUrl: './versement-update.component.html',
})
export class VersementUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    commentaire: [],
    montant: [],
    resteAVerser: [],
    lieuVersement: [],
    referenceVersement: [],
    identiteReceveur: [],
    operateur: [],
  });

  constructor(
    protected versementService: VersementService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ versement }) => {
      this.updateForm(versement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const versement = this.createFromForm();
    if (versement.id !== undefined) {
      this.subscribeToSaveResponse(this.versementService.update(versement));
    } else {
      this.subscribeToSaveResponse(this.versementService.create(versement));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVersement>>): void {
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

  protected updateForm(versement: IVersement): void {
    this.editForm.patchValue({
      id: versement.id,
      numero: versement.numero,
      commentaire: versement.commentaire,
      montant: versement.montant,
      resteAVerser: versement.resteAVerser,
      lieuVersement: versement.lieuVersement,
      referenceVersement: versement.referenceVersement,
      identiteReceveur: versement.identiteReceveur,
      operateur: versement.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      versement.operateur
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

  protected createFromForm(): IVersement {
    return {
      ...new Versement(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      resteAVerser: this.editForm.get(['resteAVerser'])!.value,
      lieuVersement: this.editForm.get(['lieuVersement'])!.value,
      referenceVersement: this.editForm.get(['referenceVersement'])!.value,
      identiteReceveur: this.editForm.get(['identiteReceveur'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
