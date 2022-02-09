import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommandeMedicament } from '../commande-medicament.model';

@Component({
  selector: 'jhi-commande-medicament-detail',
  templateUrl: './commande-medicament-detail.component.html',
})
export class CommandeMedicamentDetailComponent implements OnInit {
  commandeMedicament: ICommandeMedicament | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeMedicament }) => {
      this.commandeMedicament = commandeMedicament;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
