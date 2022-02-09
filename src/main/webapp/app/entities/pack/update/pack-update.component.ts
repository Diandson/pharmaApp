import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPack, Pack } from '../pack.model';
import { PackService } from '../service/pack.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { ITypePack } from 'app/entities/type-pack/type-pack.model';
import { TypePackService } from 'app/entities/type-pack/service/type-pack.service';

@Component({
  selector: 'jhi-pack-update',
  templateUrl: './pack-update.component.html',
})
export class PackUpdateComponent implements OnInit {
  isSaving = false;

  structuresSharedCollection: IStructure[] = [];
  typePacksSharedCollection: ITypePack[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    durer: [],
    valide: [],
    dateRenew: [],
    operateur: [],
    type: [],
  });

  constructor(
    protected packService: PackService,
    protected structureService: StructureService,
    protected typePackService: TypePackService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pack }) => {
      if (pack.id === undefined) {
        const today = dayjs().startOf('day');
        pack.dateRenew = today;
      }

      this.updateForm(pack);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pack = this.createFromForm();
    if (pack.id !== undefined) {
      this.subscribeToSaveResponse(this.packService.update(pack));
    } else {
      this.subscribeToSaveResponse(this.packService.create(pack));
    }
  }

  trackStructureById(index: number, item: IStructure): number {
    return item.id!;
  }

  trackTypePackById(index: number, item: ITypePack): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPack>>): void {
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

  protected updateForm(pack: IPack): void {
    this.editForm.patchValue({
      id: pack.id,
      libelle: pack.libelle,
      durer: pack.durer,
      valide: pack.valide,
      dateRenew: pack.dateRenew ? pack.dateRenew.format(DATE_TIME_FORMAT) : null,
      operateur: pack.operateur,
      type: pack.type,
    });

    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      pack.operateur
    );
    this.typePacksSharedCollection = this.typePackService.addTypePackToCollectionIfMissing(this.typePacksSharedCollection, pack.type);
  }

  protected loadRelationshipsOptions(): void {
    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing(structures, this.editForm.get('operateur')!.value)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));

    this.typePackService
      .query()
      .pipe(map((res: HttpResponse<ITypePack[]>) => res.body ?? []))
      .pipe(
        map((typePacks: ITypePack[]) => this.typePackService.addTypePackToCollectionIfMissing(typePacks, this.editForm.get('type')!.value))
      )
      .subscribe((typePacks: ITypePack[]) => (this.typePacksSharedCollection = typePacks));
  }

  protected createFromForm(): IPack {
    return {
      ...new Pack(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      durer: this.editForm.get(['durer'])!.value,
      valide: this.editForm.get(['valide'])!.value,
      dateRenew: this.editForm.get(['dateRenew'])!.value ? dayjs(this.editForm.get(['dateRenew'])!.value, DATE_TIME_FORMAT) : undefined,
      operateur: this.editForm.get(['operateur'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
