import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVersement } from '../versement.model';

@Component({
  selector: 'jhi-versement-detail',
  templateUrl: './versement-detail.component.html',
})
export class VersementDetailComponent implements OnInit {
  versement: IVersement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ versement }) => {
      this.versement = versement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
