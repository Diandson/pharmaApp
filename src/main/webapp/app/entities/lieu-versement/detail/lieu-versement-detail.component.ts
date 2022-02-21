import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILieuVersement } from '../lieu-versement.model';

@Component({
  selector: 'jhi-lieu-versement-detail',
  templateUrl: './lieu-versement-detail.component.html',
})
export class LieuVersementDetailComponent implements OnInit {
  lieuVersement: ILieuVersement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lieuVersement }) => {
      this.lieuVersement = lieuVersement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
