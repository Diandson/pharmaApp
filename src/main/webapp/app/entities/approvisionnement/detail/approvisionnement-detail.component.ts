import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovisionnement } from '../approvisionnement.model';

@Component({
  selector: 'jhi-approvisionnement-detail',
  templateUrl: './approvisionnement-detail.component.html',
})
export class ApprovisionnementDetailComponent implements OnInit {
  approvisionnement: IApprovisionnement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvisionnement }) => {
      this.approvisionnement = approvisionnement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
