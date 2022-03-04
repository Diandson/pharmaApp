import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovisionnement } from '../approvisionnement.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ApprovisionnementService } from '../service/approvisionnement.service';
import { ApprovisionnementDeleteDialogComponent } from '../delete/approvisionnement-delete-dialog.component';
import {ApprovisionnementUpdateComponent} from "../update/approvisionnement-update.component";
import {ApprovisionnementDetailComponent} from "../detail/approvisionnement-detail.component";

@Component({
  selector: 'jhi-approvisionnement',
  templateUrl: './approvisionnement.component.html',
})
export class ApprovisionnementComponent implements OnInit {
  approvisionnements?: IApprovisionnement[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;

  constructor(
    protected approvisionnementService: ApprovisionnementService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.approvisionnementService
      .query()
      .subscribe(res => this.approvisionnements = res.body ?? []);
  }

  ngOnInit(): void {
    // this.handleNavigation();
    this.loadPage();
  }

  trackId(index: number, item: IApprovisionnement): number {
    return item.id!;
  }

  delete(approvisionnement: IApprovisionnement): void {
    const modalRef = this.modalService.open(ApprovisionnementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvisionnement = approvisionnement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  createOrUpdate(approvisionnement?: IApprovisionnement): void {
    const modalRef = this.modalService.open(ApprovisionnementUpdateComponent, { size: 'lg', backdrop: 'static' });
    if (approvisionnement){
      modalRef.componentInstance.approvisionnement = approvisionnement;
    }

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'succes') {
        this.loadPage();
      }
    });
  }

  detailApproviso(approvisionnement?: IApprovisionnement): void {
    const modalRef = this.modalService.open(ApprovisionnementDetailComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.approvisionnement = approvisionnement;
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

  protected onSuccess(data: IApprovisionnement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/approvisionnement'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.approvisionnements = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
