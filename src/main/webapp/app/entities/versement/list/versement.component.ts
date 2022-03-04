import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVersement } from '../versement.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { VersementService } from '../service/versement.service';
import { VersementDeleteDialogComponent } from '../delete/versement-delete-dialog.component';
import {VersementDetailComponent} from "../detail/versement-detail.component";
import {VersementUpdateComponent} from "../update/versement-update.component";

@Component({
  selector: 'jhi-versement',
  templateUrl: './versement.component.html',
})
export class VersementComponent implements OnInit {
  versements?: IVersement[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;

  constructor(
    protected versementService: VersementService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.versementService
      .query()
      .subscribe(res => this.versements = res.body ?? []);
  }

  ngOnInit(): void {
    this.loadPage();
  }

  trackId(index: number, item: IVersement): number {
    return item.id!;
  }

  delete(versement: IVersement): void {
    const modalRef = this.modalService.open(VersementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.versement = versement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
  createOrUpdate(versement?: IVersement): void {
    const modalRef = this.modalService.open(VersementUpdateComponent, { size: 'lg', backdrop: 'static' });
    if (versement){
      modalRef.componentInstance.versement = versement;
    }
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'succes') {
        this.loadPage();
      }
    });
  }

  detailVersement(versement: IVersement): void {
    const modalRef = this.modalService.open(VersementDetailComponent, { size: 'sm', backdrop: 'static' });
    modalRef.componentInstance.versement = versement;
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
      }
    });
  }

  protected onSuccess(data: IVersement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/versement'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.versements = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
