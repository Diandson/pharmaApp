import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStructure } from '../structure.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-structure-detail',
  templateUrl: './structure-detail.component.html',
})
export class StructureDetailComponent implements OnInit {
  structure: IStructure | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ structure }) => {
      this.structure = structure;
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
