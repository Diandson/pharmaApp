import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonne, Personne } from '../personne.model';
import { PersonneService } from '../service/personne.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

@Component({
  selector: 'jhi-personne-update',
  templateUrl: './personne-update.component.html',
})
export class PersonneUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  structuresSharedCollection: IStructure[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    dataNaissance: [],
    lieuNaissance: [],
    numeroDoc: [],
    profil: [],
    profilContentType: [],
    telephone: [],
    user: [],
    structure: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected personneService: PersonneService,
    protected userService: UserService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personne }) => {
      this.updateForm(personne);

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
    const personne = this.createFromForm();
    if (personne.id !== undefined) {
      this.subscribeToSaveResponse(this.personneService.update(personne));
    } else {
      this.subscribeToSaveResponse(this.personneService.create(personne));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackStructureById(index: number, item: IStructure): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonne>>): void {
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

  protected updateForm(personne: IPersonne): void {
    this.editForm.patchValue({
      id: personne.id,
      nom: personne.nom,
      prenom: personne.prenom,
      dataNaissance: personne.dataNaissance,
      lieuNaissance: personne.lieuNaissance,
      numeroDoc: personne.numeroDoc,
      profil: personne.profil,
      profilContentType: personne.profilContentType,
      telephone: personne.telephone,
      user: personne.user,
      structure: personne.structure,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, personne.user);
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing(
      this.structuresSharedCollection,
      personne.structure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

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

  protected createFromForm(): IPersonne {
    return {
      ...new Personne(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dataNaissance: this.editForm.get(['dataNaissance'])!.value,
      lieuNaissance: this.editForm.get(['lieuNaissance'])!.value,
      numeroDoc: this.editForm.get(['numeroDoc'])!.value,
      profilContentType: this.editForm.get(['profilContentType'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      user: this.editForm.get(['user'])!.value,
      structure: this.editForm.get(['structure'])!.value,
    };
  }
}
