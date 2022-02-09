import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonne } from '../personne.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-personne-detail',
  templateUrl: './personne-detail.component.html',
})
export class PersonneDetailComponent implements OnInit {
  personne: IPersonne | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personne }) => {
      this.personne = personne;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
