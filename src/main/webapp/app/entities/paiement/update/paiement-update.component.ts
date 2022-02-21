import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPaiement, Paiement } from '../paiement.model';
import { PaiementService } from '../service/paiement.service';
import { IVersement } from 'app/entities/versement/versement.model';
import { VersementService } from 'app/entities/versement/service/versement.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

@Component({
  selector: 'jhi-paiement-update',
  templateUrl: './paiement-update.component.html',
})
export class PaiementUpdateComponent implements OnInit {
  isSaving = false;

  versementsSharedCollection: IVersement[] = [];
  personnesSharedCollection: IPersonne[] = [];
  ventesCollection: IVente[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    numeroVente: [],
    datePaiement: [],
    sommeRecu: [],
    sommeDonner: [],
    avoir: [],
    versement: [],
    operateur: [],
    vente: [],
  });

  constructor(
    protected paiementService: PaiementService,
    protected versementService: VersementService,
    protected personneService: PersonneService,
    protected venteService: VenteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiement }) => {
      if (paiement.id === undefined) {
        const today = dayjs().startOf('day');
        paiement.datePaiement = today;
      }

      this.updateForm(paiement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiement = this.createFromForm();
    if (paiement.id !== undefined) {
      this.subscribeToSaveResponse(this.paiementService.update(paiement));
    } else {
      this.subscribeToSaveResponse(this.paiementService.create(paiement));
    }
  }

  trackVersementById(index: number, item: IVersement): number {
    return item.id!;
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  trackVenteById(index: number, item: IVente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiement>>): void {
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

  protected updateForm(paiement: IPaiement): void {
    this.editForm.patchValue({
      id: paiement.id,
      numero: paiement.numero,
      numeroVente: paiement.numeroVente,
      datePaiement: paiement.datePaiement ? paiement.datePaiement.format(DATE_TIME_FORMAT) : null,
      sommeRecu: paiement.sommeRecu,
      sommeDonner: paiement.sommeDonner,
      avoir: paiement.avoir,
      versement: paiement.versement,
      operateur: paiement.operateur,
      vente: paiement.vente,
    });

    this.versementsSharedCollection = this.versementService.addVersementToCollectionIfMissing(
      this.versementsSharedCollection,
      paiement.versement
    );
    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      paiement.operateur
    );
    this.ventesCollection = this.venteService.addVenteToCollectionIfMissing(this.ventesCollection, paiement.vente);
  }

  protected loadRelationshipsOptions(): void {
    this.versementService
      .query()
      .pipe(map((res: HttpResponse<IVersement[]>) => res.body ?? []))
      .pipe(
        map((versements: IVersement[]) =>
          this.versementService.addVersementToCollectionIfMissing(versements, this.editForm.get('versement')!.value)
        )
      )
      .subscribe((versements: IVersement[]) => (this.versementsSharedCollection = versements));

    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing(personnes, this.editForm.get('operateur')!.value)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));

    this.venteService
      .query({ filter: 'paiement-is-null' })
      .pipe(map((res: HttpResponse<IVente[]>) => res.body ?? []))
      .pipe(map((ventes: IVente[]) => this.venteService.addVenteToCollectionIfMissing(ventes, this.editForm.get('vente')!.value)))
      .subscribe((ventes: IVente[]) => (this.ventesCollection = ventes));
  }

  protected createFromForm(): IPaiement {
    return {
      ...new Paiement(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      numeroVente: this.editForm.get(['numeroVente'])!.value,
      datePaiement: this.editForm.get(['datePaiement'])!.value
        ? dayjs(this.editForm.get(['datePaiement'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sommeRecu: this.editForm.get(['sommeRecu'])!.value,
      sommeDonner: this.editForm.get(['sommeDonner'])!.value,
      avoir: this.editForm.get(['avoir'])!.value,
      versement: this.editForm.get(['versement'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
      vente: this.editForm.get(['vente'])!.value,
    };
  }
}
