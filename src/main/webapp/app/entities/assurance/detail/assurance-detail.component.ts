import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssurance } from '../assurance.model';

@Component({
  selector: 'jhi-assurance-detail',
  templateUrl: './assurance-detail.component.html',
})
export class AssuranceDetailComponent implements OnInit {
  assurance: IAssurance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assurance }) => {
      this.assurance = assurance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
