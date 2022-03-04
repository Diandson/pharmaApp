import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVersement } from '../versement.model';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-versement-detail',
  templateUrl: './versement-detail.component.html',
})
export class VersementDetailComponent implements OnInit {
  versement: IVersement | null = null;
  lolo?: string;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ versement }) => {
    //   this.versement = versement;
    // });
    this.lolo = 'rien';
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }
}
