import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import {DATE_TIME_FORMAT} from 'app/config/input.constants';

import {IVente, Vente} from '../vente.model';
import {VenteService} from '../service/vente.service';
import {IPersonne} from 'app/entities/personne/personne.model';
import {PersonneService} from 'app/entities/personne/service/personne.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-vente-update',
  templateUrl: './vente-update.component.html',
})
export class VenteUpdateComponent implements OnInit {
  isSaving = false;
  vente: IVente = new Vente()

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    dateVente: [],
    montant: [],
    montantAssurance: [],
    sommeDonne: [],
    sommeRendu: [],
    avoir: [],
    operateur: [],
  });

  constructor(
    protected venteService: VenteService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vente }) => {
      if (vente) {
        vente.dateVente = dayjs().startOf('day');
      }
    });

    if (this.vente.id){
      this.updateForm(this.vente);
    }
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    const vente = this.createFromForm();
    if (vente.id) {
      this.subscribeToSaveResponse(this.venteService.update(vente));
    } else {
      this.subscribeToSaveResponse(this.venteService.create(vente));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVente>>): void {
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

  protected updateForm(vente: IVente): void {
    this.editForm.patchValue({
      id: vente.id,
      numero: vente.numero,
      dateVente: vente.dateVente ? vente.dateVente.format(DATE_TIME_FORMAT) : null,
      montant: vente.montant,
      montantAssurance: vente.montantAssurance,
      sommeDonne: vente.sommeDonne,
      sommeRendu: vente.sommeRendu,
      avoir: vente.avoir,
      operateur: vente.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(this.personnesSharedCollection, vente.operateur);
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IVente {
    return {
      ...new Vente(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateVente: this.editForm.get(['dateVente'])!.value ? dayjs(this.editForm.get(['dateVente'])!.value, DATE_TIME_FORMAT) : undefined,
      montant: this.editForm.get(['montant'])!.value,
      montantAssurance: this.editForm.get(['montantAssurance'])!.value,
      sommeDonne: this.editForm.get(['sommeDonne'])!.value,
      sommeRendu: this.editForm.get(['sommeRendu'])!.value,
      avoir: this.editForm.get(['avoir'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
