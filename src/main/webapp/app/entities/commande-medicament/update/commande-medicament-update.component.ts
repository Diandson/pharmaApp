import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommandeMedicament, CommandeMedicament } from '../commande-medicament.model';
import { CommandeMedicamentService } from '../service/commande-medicament.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  selector: 'jhi-commande-medicament-update',
  templateUrl: './commande-medicament-update.component.html',
})
export class CommandeMedicamentUpdateComponent implements OnInit {
  isSaving = false;

  medicamentsSharedCollection: IMedicament[] = [];
  commandesSharedCollection: ICommande[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    medicament: [],
    commande: [],
  });

  constructor(
    protected commandeMedicamentService: CommandeMedicamentService,
    protected medicamentService: MedicamentService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeMedicament }) => {
      this.updateForm(commandeMedicament);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandeMedicament = this.createFromForm();
    if (commandeMedicament.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeMedicamentService.update(commandeMedicament));
    } else {
      this.subscribeToSaveResponse(this.commandeMedicamentService.create(commandeMedicament));
    }
  }

  trackMedicamentById(index: number, item: IMedicament): number {
    return item.id!;
  }

  trackCommandeById(index: number, item: ICommande): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandeMedicament>>): void {
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

  protected updateForm(commandeMedicament: ICommandeMedicament): void {
    this.editForm.patchValue({
      id: commandeMedicament.id,
      quantite: commandeMedicament.quantite,
      medicament: commandeMedicament.medicament,
      commande: commandeMedicament.commande,
    });

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing(
      this.medicamentsSharedCollection,
      commandeMedicament.medicament
    );
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      commandeMedicament.commande
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

    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));
  }

  protected createFromForm(): ICommandeMedicament {
    return {
      ...new CommandeMedicament(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      medicament: this.editForm.get(['medicament'])!.value,
      commande: this.editForm.get(['commande'])!.value,
    };
  }
}
