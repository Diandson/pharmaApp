import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInventaireMedicament, InventaireMedicament } from '../inventaire-medicament.model';
import { InventaireMedicamentService } from '../service/inventaire-medicament.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IInventaire } from 'app/entities/inventaire/inventaire.model';
import { InventaireService } from 'app/entities/inventaire/service/inventaire.service';

@Component({
  selector: 'jhi-inventaire-medicament-update',
  templateUrl: './inventaire-medicament-update.component.html',
})
export class InventaireMedicamentUpdateComponent implements OnInit {
  isSaving = false;

  medicamentsSharedCollection: IMedicament[] = [];
  inventairesSharedCollection: IInventaire[] = [];

  editForm = this.fb.group({
    id: [],
    stockTheorique: [],
    stockPhysique: [],
    stockDifferent: [],
    dateFabrication: [],
    dateExpiration: [],
    medicament: [],
    inventaire: [],
  });

  constructor(
    protected inventaireMedicamentService: InventaireMedicamentService,
    protected medicamentService: MedicamentService,
    protected inventaireService: InventaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventaireMedicament }) => {
      this.updateForm(inventaireMedicament);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventaireMedicament = this.createFromForm();
    if (inventaireMedicament.id !== undefined) {
      this.subscribeToSaveResponse(this.inventaireMedicamentService.update(inventaireMedicament));
    } else {
      this.subscribeToSaveResponse(this.inventaireMedicamentService.create(inventaireMedicament));
    }
  }

  trackMedicamentById(index: number, item: IMedicament): number {
    return item.id!;
  }

  trackInventaireById(index: number, item: IInventaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventaireMedicament>>): void {
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

  protected updateForm(inventaireMedicament: IInventaireMedicament): void {
    this.editForm.patchValue({
      id: inventaireMedicament.id,
      stockTheorique: inventaireMedicament.stockTheorique,
      stockPhysique: inventaireMedicament.stockPhysique,
      stockDifferent: inventaireMedicament.stockDifferent,
      dateFabrication: inventaireMedicament.dateFabrication,
      dateExpiration: inventaireMedicament.dateExpiration,
      medicament: inventaireMedicament.medicament,
      inventaire: inventaireMedicament.inventaire,
    });

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing(
      this.medicamentsSharedCollection,
      inventaireMedicament.medicament
    );
    this.inventairesSharedCollection = this.inventaireService.addInventaireToCollectionIfMissing(
      this.inventairesSharedCollection,
      inventaireMedicament.inventaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing(medicaments, this.editForm.get('medicament')!.value)
        )
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));

    this.inventaireService
      .query()
      .pipe(map((res: HttpResponse<IInventaire[]>) => res.body ?? []))
      .pipe(
        map((inventaires: IInventaire[]) =>
          this.inventaireService.addInventaireToCollectionIfMissing(inventaires, this.editForm.get('inventaire')!.value)
        )
      )
      .subscribe((inventaires: IInventaire[]) => (this.inventairesSharedCollection = inventaires));
  }

  protected createFromForm(): IInventaireMedicament {
    return {
      ...new InventaireMedicament(),
      id: this.editForm.get(['id'])!.value,
      stockTheorique: this.editForm.get(['stockTheorique'])!.value,
      stockPhysique: this.editForm.get(['stockPhysique'])!.value,
      stockDifferent: this.editForm.get(['stockDifferent'])!.value,
      dateFabrication: this.editForm.get(['dateFabrication'])!.value,
      dateExpiration: this.editForm.get(['dateExpiration'])!.value,
      medicament: this.editForm.get(['medicament'])!.value,
      inventaire: this.editForm.get(['inventaire'])!.value,
    };
  }
}
