import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVenteMedicament, VenteMedicament } from '../vente-medicament.model';
import { VenteMedicamentService } from '../service/vente-medicament.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

@Component({
  selector: 'jhi-vente-medicament-update',
  templateUrl: './vente-medicament-update.component.html',
})
export class VenteMedicamentUpdateComponent implements OnInit {
  isSaving = false;

  medicamentsSharedCollection: IMedicament[] = [];
  ventesSharedCollection: IVente[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    montant: [],
    medicament: [],
    vente: [],
  });

  constructor(
    protected venteMedicamentService: VenteMedicamentService,
    protected medicamentService: MedicamentService,
    protected venteService: VenteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venteMedicament }) => {
      this.updateForm(venteMedicament);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venteMedicament = this.createFromForm();
    if (venteMedicament.id !== undefined) {
      this.subscribeToSaveResponse(this.venteMedicamentService.update(venteMedicament));
    } else {
      this.subscribeToSaveResponse(this.venteMedicamentService.create(venteMedicament));
    }
  }

  trackMedicamentById(index: number, item: IMedicament): number {
    return item.id!;
  }

  trackVenteById(index: number, item: IVente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenteMedicament>>): void {
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

  protected updateForm(venteMedicament: IVenteMedicament): void {
    this.editForm.patchValue({
      id: venteMedicament.id,
      quantite: venteMedicament.quantite,
      montant: venteMedicament.montant,
      medicament: venteMedicament.medicament,
      vente: venteMedicament.vente,
    });

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing(
      this.medicamentsSharedCollection,
      venteMedicament.medicament
    );
    this.ventesSharedCollection = this.venteService.addVenteToCollectionIfMissing(this.ventesSharedCollection, venteMedicament.vente);
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

    this.venteService
      .query()
      .pipe(map((res: HttpResponse<IVente[]>) => res.body ?? []))
      .pipe(map((ventes: IVente[]) => this.venteService.addVenteToCollectionIfMissing(ventes, this.editForm.get('vente')!.value)))
      .subscribe((ventes: IVente[]) => (this.ventesSharedCollection = ventes));
  }

  protected createFromForm(): IVenteMedicament {
    return {
      ...new VenteMedicament(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      medicament: this.editForm.get(['medicament'])!.value,
      vente: this.editForm.get(['vente'])!.value,
    };
  }
}
