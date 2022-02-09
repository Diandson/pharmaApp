import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { ILivraison } from 'app/entities/livraison/livraison.model';
import { LivraisonService } from 'app/entities/livraison/service/livraison.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  livraisonsCollection: ILivraison[] = [];
  fournisseursCollection: IFournisseur[] = [];
  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    dateCommande: [],
    livraison: [],
    fournisseur: [],
    operateur: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected livraisonService: LivraisonService,
    protected fournisseurService: FournisseurService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      if (commande.id === undefined) {
        const today = dayjs().startOf('day');
        commande.dateCommande = today;
      }

      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackLivraisonById(index: number, item: ILivraison): number {
    return item.id!;
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      numero: commande.numero,
      dateCommande: commande.dateCommande ? commande.dateCommande.format(DATE_TIME_FORMAT) : null,
      livraison: commande.livraison,
      fournisseur: commande.fournisseur,
      operateur: commande.operateur,
    });

    this.livraisonsCollection = this.livraisonService.addLivraisonToCollectionIfMissing(this.livraisonsCollection, commande.livraison);
    this.fournisseursCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursCollection,
      commande.fournisseur
    );
    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      commande.operateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.livraisonService
      .query({ filter: 'commande-is-null' })
      .pipe(map((res: HttpResponse<ILivraison[]>) => res.body ?? []))
      .pipe(
        map((livraisons: ILivraison[]) =>
          this.livraisonService.addLivraisonToCollectionIfMissing(livraisons, this.editForm.get('livraison')!.value)
        )
      )
      .subscribe((livraisons: ILivraison[]) => (this.livraisonsCollection = livraisons));

    this.fournisseurService
      .query({ filter: 'commande-is-null' })
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing(fournisseurs, this.editForm.get('fournisseur')!.value)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursCollection = fournisseurs));

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

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateCommande: this.editForm.get(['dateCommande'])!.value
        ? dayjs(this.editForm.get(['dateCommande'])!.value, DATE_TIME_FORMAT)
        : undefined,
      livraison: this.editForm.get(['livraison'])!.value,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
