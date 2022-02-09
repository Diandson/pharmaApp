import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPack } from '../pack.model';

@Component({
  selector: 'jhi-pack-detail',
  templateUrl: './pack-detail.component.html',
})
export class PackDetailComponent implements OnInit {
  pack: IPack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pack }) => {
      this.pack = pack;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
