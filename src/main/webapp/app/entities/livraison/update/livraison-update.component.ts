import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILivraison, Livraison } from '../livraison.model';
import { LivraisonService } from '../service/livraison.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-livraison-update',
  templateUrl: './livraison-update.component.html',
})
export class LivraisonUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    dateLivraison: [],
    operateur: [],
  });

  constructor(
    protected livraisonService: LivraisonService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livraison }) => {
      if (livraison.id === undefined) {
        const today = dayjs().startOf('day');
        livraison.dateLivraison = today;
      }

      this.updateForm(livraison);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livraison = this.createFromForm();
    if (livraison.id !== undefined) {
      this.subscribeToSaveResponse(this.livraisonService.update(livraison));
    } else {
      this.subscribeToSaveResponse(this.livraisonService.create(livraison));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivraison>>): void {
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

  protected updateForm(livraison: ILivraison): void {
    this.editForm.patchValue({
      id: livraison.id,
      numero: livraison.numero,
      dateLivraison: livraison.dateLivraison ? livraison.dateLivraison.format(DATE_TIME_FORMAT) : null,
      operateur: livraison.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      livraison.operateur
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

  protected createFromForm(): ILivraison {
    return {
      ...new Livraison(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateLivraison: this.editForm.get(['dateLivraison'])!.value
        ? dayjs(this.editForm.get(['dateLivraison'])!.value, DATE_TIME_FORMAT)
        : undefined,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
