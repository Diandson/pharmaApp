import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFournisseur, Fournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-fournisseur-update',
  templateUrl: './fournisseur-update.component.html',
})
export class FournisseurUpdateComponent implements OnInit {
  isSaving = false;
  fournisseur?: IFournisseur;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    email: [],
    operateur: [],
  });

  constructor(
    protected fournisseurService: FournisseurService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ fournisseur }) => {
    //   this.updateForm(fournisseur);
    //
    //   this.loadRelationshipsOptions();
    // });
    if (this.fournisseur!.id){
      this.updateForm(this.fournisseur!);
    }
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    const fournisseur = this.createFromForm();
    if (fournisseur.id) {
      this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.create(fournisseur));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFournisseur>>): void {
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

  protected updateForm(fournisseur: IFournisseur): void {
    this.editForm.patchValue({
      id: fournisseur.id,
      libelle: fournisseur.libelle,
      email: fournisseur.email,
      operateur: fournisseur.operateur,
    });
  }

  protected createFromForm(): IFournisseur {
    return {
      ...new Fournisseur(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      email: this.editForm.get(['email'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
