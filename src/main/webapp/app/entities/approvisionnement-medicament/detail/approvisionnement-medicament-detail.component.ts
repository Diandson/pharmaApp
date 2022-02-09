import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovisionnementMedicament } from '../approvisionnement-medicament.model';

@Component({
  selector: 'jhi-approvisionnement-medicament-detail',
  templateUrl: './approvisionnement-medicament-detail.component.html',
})
export class ApprovisionnementMedicamentDetailComponent implements OnInit {
  approvisionnementMedicament: IApprovisionnementMedicament | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ approvisionnementMedicament }) => {
      this.approvisionnementMedicament = approvisionnementMedicament;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
