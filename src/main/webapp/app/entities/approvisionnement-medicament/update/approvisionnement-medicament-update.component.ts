import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprovisionnementMedicament, ApprovisionnementMedicament } from '../approvisionnement-medicament.model';
import { ApprovisionnementMedicamentService } from '../service/approvisionnement-medicament.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IApprovisionnement } from 'app/entities/approvisionnement/approvisionnement.model';
import { ApprovisionnementService } from 'app/entities/approvisionnement/service/approvisionnement.service';

@Component({
  selector: 'jhi-approvisionnement-medicament-update',
  templateUrl: './approvisionnement-medicament-update.component.html',
})
export class ApprovisionnementMedicamentUpdateComponent implements OnInit {
  isSaving = false;

  medicamentsSharedCollection: IMedicament[] = [];
  approvisionnementsSharedCollection: IApprovisionnement[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    medicament: [],
    approvionnement: [],
  });

  constructor(
    protected approvisionnementMedicamentService: ApprovisionnementMedicamentService,
    protected medicamentService: MedicamentService,
    protected approvisionnementService: ApprovisionnementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvisionnementMedicament }) => {
      this.updateForm(approvisionnementMedicament);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const approvisionnementMedicament = this.createFromForm();
    if (approvisionnementMedicament.id !== undefined) {
      this.subscribeToSaveResponse(this.approvisionnementMedicamentService.update(approvisionnementMedicament));
    } else {
      this.subscribeToSaveResponse(this.approvisionnementMedicamentService.create(approvisionnementMedicament));
    }
  }

  trackMedicamentById(index: number, item: IMedicament): number {
    return item.id!;
  }

  trackApprovisionnementById(index: number, item: IApprovisionnement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprovisionnementMedicament>>): void {
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

  protected updateForm(approvisionnementMedicament: IApprovisionnementMedicament): void {
    this.editForm.patchValue({
      id: approvisionnementMedicament.id,
      quantite: approvisionnementMedicament.quantite,
      medicament: approvisionnementMedicament.medicament,
      approvionnement: approvisionnementMedicament.approvionnement,
    });

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing(
      this.medicamentsSharedCollection,
      approvisionnementMedicament.medicament
    );
    this.approvisionnementsSharedCollection = this.approvisionnementService.addApprovisionnementToCollectionIfMissing(
      this.approvisionnementsSharedCollection,
      approvisionnementMedicament.approvionnement
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

    this.approvisionnementService
      .query()
      .pipe(map((res: HttpResponse<IApprovisionnement[]>) => res.body ?? []))
      .pipe(
        map((approvisionnements: IApprovisionnement[]) =>
          this.approvisionnementService.addApprovisionnementToCollectionIfMissing(
            approvisionnements,
            this.editForm.get('approvionnement')!.value
          )
        )
      )
      .subscribe((approvisionnements: IApprovisionnement[]) => (this.approvisionnementsSharedCollection = approvisionnements));
  }

  protected createFromForm(): IApprovisionnementMedicament {
    return {
      ...new ApprovisionnementMedicament(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      medicament: this.editForm.get(['medicament'])!.value,
      approvionnement: this.editForm.get(['approvionnement'])!.value,
    };
  }
}
