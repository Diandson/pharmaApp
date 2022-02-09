import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { IAssurance } from 'app/entities/assurance/assurance.model';
import { AssuranceService } from 'app/entities/assurance/service/assurance.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  assurancesSharedCollection: IAssurance[] = [];
  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    telephone: [],
    numeroAssure: [],
    assurance: [],
    operateur: [],
  });

  constructor(
    protected clientService: ClientService,
    protected assuranceService: AssuranceService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackAssuranceById(index: number, item: IAssurance): number {
    return item.id!;
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      nom: client.nom,
      prenom: client.prenom,
      telephone: client.telephone,
      numeroAssure: client.numeroAssure,
      assurance: client.assurance,
      operateur: client.operateur,
    });

    this.assurancesSharedCollection = this.assuranceService.addAssuranceToCollectionIfMissing(
      this.assurancesSharedCollection,
      client.assurance
    );
    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      client.operateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assuranceService
      .query()
      .pipe(map((res: HttpResponse<IAssurance[]>) => res.body ?? []))
      .pipe(
        map((assurances: IAssurance[]) =>
          this.assuranceService.addAssuranceToCollectionIfMissing(assurances, this.editForm.get('assurance')!.value)
        )
      )
      .subscribe((assurances: IAssurance[]) => (this.assurancesSharedCollection = assurances));

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

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      numeroAssure: this.editForm.get(['numeroAssure'])!.value,
      assurance: this.editForm.get(['assurance'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
