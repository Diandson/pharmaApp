import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedicament, Medicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

@Component({
  selector: 'jhi-medicament-update',
  templateUrl: './medicament-update.component.html',
})
export class MedicamentUpdateComponent implements OnInit {
  isSaving = false;

  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    denomination: [],
    dci: [],
    forme: [],
    dosage: [],
    classe: [],
    codeBare: [null, [Validators.maxLength(5000)]],
    prixAchat: [],
    prixPublic: [],
    stockAlerte: [],
    stockSecurite: [],
    stockTheorique: [],
    dateFabrication: [],
    dateExpiration: [],
    image: [],
    imageContentType: [],
    structure: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected medicamentService: MedicamentService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicament }) => {
      this.updateForm(medicament);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('appPharmaApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicament = this.createFromForm();
    if (medicament.id !== undefined) {
      this.subscribeToSaveResponse(this.medicamentService.update(medicament));
    } else {
      this.subscribeToSaveResponse(this.medicamentService.create(medicament));
    }
  }

  trackStructureById(index: number, item: IStructure): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicament>>): void {
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

  protected updateForm(medicament: IMedicament): void {
    this.editForm.patchValue({
      id: medicament.id,
      denomination: medicament.denomination,
      dci: medicament.dci,
      forme: medicament.forme,
      dosage: medicament.dosage,
      classe: medicament.classe,
      codeBare: medicament.codeBare,
      prixAchat: medicament.prixAchat,
      prixPublic: medicament.prixPublic,
      stockAlerte: medicament.stockAlerte,
      stockSecurite: medicament.stockSecurite,
      stockTheorique: medicament.stockTheorique,
      dateFabrication: medicament.dateFabrication,
      dateExpiration: medicament.dateExpiration,
      image: medicament.image,
      imageContentType: medicament.imageContentType,
      structure: medicament.structure,
    });

    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      medicament.structure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing(structures, this.editForm.get('structure')!.value)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));
  }

  protected createFromForm(): IMedicament {
    return {
      ...new Medicament(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      dci: this.editForm.get(['dci'])!.value,
      forme: this.editForm.get(['forme'])!.value,
      dosage: this.editForm.get(['dosage'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      codeBare: this.editForm.get(['codeBare'])!.value,
      prixAchat: this.editForm.get(['prixAchat'])!.value,
      prixPublic: this.editForm.get(['prixPublic'])!.value,
      stockAlerte: this.editForm.get(['stockAlerte'])!.value,
      stockSecurite: this.editForm.get(['stockSecurite'])!.value,
      stockTheorique: this.editForm.get(['stockTheorique'])!.value,
      dateFabrication: this.editForm.get(['dateFabrication'])!.value,
      dateExpiration: this.editForm.get(['dateExpiration'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      structure: this.editForm.get(['structure'])!.value,
    };
  }
}
