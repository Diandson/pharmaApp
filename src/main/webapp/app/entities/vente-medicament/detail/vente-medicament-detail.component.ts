import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVenteMedicament } from '../vente-medicament.model';

@Component({
  selector: 'jhi-vente-medicament-detail',
  templateUrl: './vente-medicament-detail.component.html',
})
export class VenteMedicamentDetailComponent implements OnInit {
  venteMedicament: IVenteMedicament | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venteMedicament }) => {
      this.venteMedicament = venteMedicament;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
