import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepense } from '../depense.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { DepenseService } from '../service/depense.service';
import { DepenseDeleteDialogComponent } from '../delete/depense-delete-dialog.component';
import {DepenseUpdateComponent} from "../update/depense-update.component";
import {DepenseDetailComponent} from "../detail/depense-detail.component";

@Component({
  selector: 'jhi-depense',
  templateUrl: './depense.component.html',
})
export class DepenseComponent implements OnInit {
  depenses?: IDepense[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;

  constructor(
    protected depenseService: DepenseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.depenseService
      .query()
      .subscribe(res => this.depenses = res.body ?? []);
  }

  ngOnInit(): void {
    this.loadPage();
  }

  trackId(index: number, item: IDepense): number {
    return item.id!;
  }

  delete(depense: IDepense): void {
    const modalRef = this.modalService.open(DepenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.depense = depense;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  createOrUpdate(depense?: IDepense): void {
    const modalRef = this.modalService.open(DepenseUpdateComponent, { size: 'lg', backdrop: 'static' });
    if (depense){
      modalRef.componentInstance.depense = depense;
    }

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'succes') {
        this.loadPage();
      }
    });
  }
  viewDepense(depense: IDepense):void {
    const modalRef = this.modalService.open(DepenseDetailComponent, { size: 'sm', backdrop: 'static' });
    modalRef.componentInstance.depense = depense;
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

  protected onSuccess(data: IDepense[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/depense'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.depenses = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
