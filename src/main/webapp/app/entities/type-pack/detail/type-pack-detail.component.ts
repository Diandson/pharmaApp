import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePack } from '../type-pack.model';

@Component({
  selector: 'jhi-type-pack-detail',
  templateUrl: './type-pack-detail.component.html',
})
export class TypePackDetailComponent implements OnInit {
  typePack: ITypePack | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typePack }) => {
      this.typePack = typePack;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
