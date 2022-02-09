import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInventaire, Inventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-inventaire-update',
  templateUrl: './inventaire-update.component.html',
})
export class InventaireUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    dateInventaire: [],
    operateur: [],
  });

  constructor(
    protected inventaireService: InventaireService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventaire }) => {
      this.updateForm(inventaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventaire = this.createFromForm();
    if (inventaire.id !== undefined) {
      this.subscribeToSaveResponse(this.inventaireService.update(inventaire));
    } else {
      this.subscribeToSaveResponse(this.inventaireService.create(inventaire));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventaire>>): void {
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

  protected updateForm(inventaire: IInventaire): void {
    this.editForm.patchValue({
      id: inventaire.id,
      numero: inventaire.numero,
      dateInventaire: inventaire.dateInventaire,
      operateur: inventaire.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      inventaire.operateur
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

  protected createFromForm(): IInventaire {
    return {
      ...new Inventaire(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateInventaire: this.editForm.get(['dateInventaire'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
