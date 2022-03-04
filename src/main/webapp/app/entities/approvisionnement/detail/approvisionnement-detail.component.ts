import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApprovisionnement } from '../approvisionnement.model';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-approvisionnement-detail',
  templateUrl: './approvisionnement-detail.component.html',
})
export class ApprovisionnementDetailComponent implements OnInit {
  approvisionnement: IApprovisionnement | null = null;
  lolo?: string;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ approvisionnement }) => {
    //   this.approvisionnement = approvisionnement;
    // });
    this.lolo = 'rien'
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }
}
