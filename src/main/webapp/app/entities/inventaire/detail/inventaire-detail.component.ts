import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInventaire } from '../inventaire.model';

@Component({
  selector: 'jhi-inventaire-detail',
  templateUrl: './inventaire-detail.component.html',
})
export class InventaireDetailComponent implements OnInit {
  inventaire: IInventaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventaire }) => {
      this.inventaire = inventaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
