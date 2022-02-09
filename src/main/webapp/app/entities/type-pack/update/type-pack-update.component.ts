import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypePack, TypePack } from '../type-pack.model';
import { TypePackService } from '../service/type-pack.service';

@Component({
  selector: 'jhi-type-pack-update',
  templateUrl: './type-pack-update.component.html',
})
export class TypePackUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    durer: [],
    prix: [],
    annexe: [],
  });

  constructor(protected typePackService: TypePackService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePack }) => {
      this.updateForm(typePack);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typePack = this.createFromForm();
    if (typePack.id !== undefined) {
      this.subscribeToSaveResponse(this.typePackService.update(typePack));
    } else {
      this.subscribeToSaveResponse(this.typePackService.create(typePack));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePack>>): void {
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

  protected updateForm(typePack: ITypePack): void {
    this.editForm.patchValue({
      id: typePack.id,
      libelle: typePack.libelle,
      durer: typePack.durer,
      prix: typePack.prix,
      annexe: typePack.annexe,
    });
  }

  protected createFromForm(): ITypePack {
    return {
      ...new TypePack(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      durer: this.editForm.get(['durer'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      annexe: this.editForm.get(['annexe'])!.value,
    };
  }
}
