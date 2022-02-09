import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInventaireMedicament } from '../inventaire-medicament.model';

@Component({
  selector: 'jhi-inventaire-medicament-detail',
  templateUrl: './inventaire-medicament-detail.component.html',
})
export class InventaireMedicamentDetailComponent implements OnInit {
  inventaireMedicament: IInventaireMedicament | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventaireMedicament }) => {
      this.inventaireMedicament = inventaireMedicament;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
