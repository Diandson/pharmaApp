import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStructure, Structure } from '../structure.model';
import { StructureService } from '../service/structure.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';

@Component({
  selector: 'jhi-structure-update',
  templateUrl: './structure-update.component.html',
})
export class StructureUpdateComponent implements OnInit {
  isSaving = false;
  typeStructureValues = Object.keys(TypeStructure);

  editForm = this.fb.group({
    id: [],
    denomination: [],
    ifu: [],
    rccm: [],
    codePostal: [],
    localisation: [],
    contact: [],
    regime: [],
    division: [],
    email: [],
    logo: [],
    logoContentType: [],
    cachet: [],
    cachetContentType: [],
    signature: [],
    signatureContentType: [],
    dateConfig: [],
    pdg: [],
    type: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ structure }) => {
      if (structure.id === undefined) {
        const today = dayjs().startOf('day');
        structure.dateConfig = today;
      }

      this.updateForm(structure);
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
    const structure = this.createFromForm();
    if (structure.id !== undefined) {
      this.subscribeToSaveResponse(this.structureService.update(structure));
    } else {
      this.subscribeToSaveResponse(this.structureService.create(structure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStructure>>): void {
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

  protected updateForm(structure: IStructure): void {
    this.editForm.patchValue({
      id: structure.id,
      denomination: structure.denomination,
      ifu: structure.ifu,
      rccm: structure.rccm,
      codePostal: structure.codePostal,
      localisation: structure.localisation,
      contact: structure.contact,
      regime: structure.regime,
      division: structure.division,
      email: structure.email,
      logo: structure.logo,
      logoContentType: structure.logoContentType,
      cachet: structure.cachet,
      cachetContentType: structure.cachetContentType,
      signature: structure.signature,
      signatureContentType: structure.signatureContentType,
      dateConfig: structure.dateConfig ? structure.dateConfig.format(DATE_TIME_FORMAT) : null,
      pdg: structure.pdg,
      type: structure.type,
    });
  }

  protected createFromForm(): IStructure {
    return {
      ...new Structure(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      ifu: this.editForm.get(['ifu'])!.value,
      rccm: this.editForm.get(['rccm'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      localisation: this.editForm.get(['localisation'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      regime: this.editForm.get(['regime'])!.value,
      division: this.editForm.get(['division'])!.value,
      email: this.editForm.get(['email'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      cachetContentType: this.editForm.get(['cachetContentType'])!.value,
      cachet: this.editForm.get(['cachet'])!.value,
      signatureContentType: this.editForm.get(['signatureContentType'])!.value,
      signature: this.editForm.get(['signature'])!.value,
      dateConfig: this.editForm.get(['dateConfig'])!.value ? dayjs(this.editForm.get(['dateConfig'])!.value, DATE_TIME_FORMAT) : undefined,
      pdg: this.editForm.get(['pdg'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
