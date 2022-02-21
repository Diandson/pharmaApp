import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMotifListeDepense } from '../motif-liste-depense.model';

@Component({
  selector: 'jhi-motif-liste-depense-detail',
  templateUrl: './motif-liste-depense-detail.component.html',
})
export class MotifListeDepenseDetailComponent implements OnInit {
  motifListeDepense: IMotifListeDepense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motifListeDepense }) => {
      this.motifListeDepense = motifListeDepense;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
