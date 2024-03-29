import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicament } from '../medicament.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { MedicamentService } from '../service/medicament.service';
import { MedicamentDeleteDialogComponent } from '../delete/medicament-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import {NzUploadFile} from "ng-zorro-antd/upload";
import {NzModalService} from "ng-zorro-antd/modal";
import {ProgressDialogComponent} from "../../../shared/progress-dialog/progress-dialog.component";

@Component({
  selector: 'jhi-medicament',
  templateUrl: './medicament.component.html',
  styleUrls: ['../medicament.component.scss']
})
export class MedicamentComponent implements OnInit {
  medicaments?: IMedicament[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;
  isModal = false;
  defaultFileList: NzUploadFile[] = [];

  constructor(
    protected medicamentService: MedicamentService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    private modal: NzModalService,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {

    this.medicamentService
      .query()
      .subscribe( res => this.medicaments = res.body ?? [])
  }

  ngOnInit(): void {
    // this.handleNavigation();
    this.loadPage();
  }

  trackId(index: number, item: IMedicament): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(medicament: IMedicament): void {
    const modalRef = this.modalService.open(MedicamentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medicament = medicament;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  uploadFile(): void {
    const dialogRef = this.modalService.open(ProgressDialogComponent,
      { backdrop: 'static', centered: true, windowClass: 'myCustomModalClass' });

    this.medicamentService.upload(this.defaultFileList[0].originFileObj).subscribe(
      res => {
        if (res.body) {
          close();
          dialogRef.close();
          this.succes('Ajouté avec succès!');
          this.loadPage();
          close();
        } else {
          close()
          dialogRef.close();
          this.warning('Un problème est survenu!');
        }
      },
      () => {
        this.isModal = false;
        dialogRef.close();
        this.error('Erreur de connexion au serveur!');
      }
    );
  }

  succes(msg: string): void {
    this.modal.success({
      nzTitle: 'SUCCESS',
      nzContent: msg,
      nzClosable: false,
      nzOkText: 'OK',
    });
  }

  error(msg: string): void {
    this.modal.error({
      nzTitle: 'ERREUR',
      nzContent: msg,
      nzClosable: false,
      nzOkText: 'OK',
    });
  }

  warning(msg: string): void {
    this.modal.warning({
      nzTitle: 'ATTENTION',
      nzContent: msg,
      nzClosable: false,
      nzOkText: 'OK',
    });
  }
  close(): void {
    this.defaultFileList = [];
    this.isModal = false;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        // this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IMedicament[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/medicament'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.medicaments = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
