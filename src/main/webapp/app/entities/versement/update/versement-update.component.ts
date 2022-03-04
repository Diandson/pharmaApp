import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVersement, Versement } from '../versement.model';
import { VersementService } from '../service/versement.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-versement-update',
  templateUrl: './versement-update.component.html',
})
export class VersementUpdateComponent implements OnInit {
  isSaving = false;
  versement: IVersement = new Versement();

  editForm = this.fb.group({
    id: [],
    numero: [],
    commentaire: [],
    montant: [],
    resteAVerser: [],
    lieuVersement: [],
    referenceVersement: [],
    identiteReceveur: [],
    operateur: [],
  });
  montant = 0;

  constructor(
    protected versementService: VersementService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ versement }) => {
    //
    //   this.loadRelationshipsOptions();
    // });
    if (this.versement.id){
      this.updateForm(this.versement);
    }
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    const versement = this.createFromForm();
    if (versement.id) {
      this.subscribeToSaveResponse(this.versementService.update(versement));
    } else {
      this.subscribeToSaveResponse(this.versementService.create(versement));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVersement>>): void {
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

  protected updateForm(versement: IVersement): void {
    this.editForm.patchValue({
      id: versement.id,
      numero: versement.numero,
      commentaire: versement.commentaire,
      montant: versement.montant,
      resteAVerser: versement.resteAVerser,
      lieuVersement: versement.lieuVersement,
      referenceVersement: versement.referenceVersement,
      identiteReceveur: versement.identiteReceveur,
      operateur: versement.operateur,
    });
  }

  protected createFromForm(): IVersement {
    return {
      ...new Versement(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      resteAVerser: this.editForm.get(['resteAVerser'])!.value,
      lieuVersement: this.editForm.get(['lieuVersement'])!.value,
      referenceVersement: this.editForm.get(['referenceVersement'])!.value,
      identiteReceveur: this.editForm.get(['identiteReceveur'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
