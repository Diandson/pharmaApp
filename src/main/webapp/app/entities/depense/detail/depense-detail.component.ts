import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepense } from '../depense.model';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-depense-detail',
  templateUrl: './depense-detail.component.html',
})
export class DepenseDetailComponent implements OnInit {
  depense: IDepense | null = null;
  lolo?: string;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected activeModel: NgbActiveModal
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ depense }) => {
    //   this.depense = depense;
    // });
    this.lolo = 'rien';
  }

  previousState(): void {
    // window.history.back();
    this.activeModel.close();
  }
}
