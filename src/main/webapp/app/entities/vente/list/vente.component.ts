import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVente } from '../vente.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { VenteService } from '../service/vente.service';
import { VenteDeleteDialogComponent } from '../delete/vente-delete-dialog.component';
import {VenteUpdateComponent} from "../update/vente-update.component";

@Component({
  selector: 'jhi-vente',
  templateUrl: './vente.component.html',
})
export class VenteComponent implements OnInit {
  ventes?: IVente[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;

  constructor(
    protected venteService: VenteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {

    this.venteService
      .query()
      .subscribe(res => this.ventes = res.body ?? []);
  }

  ngOnInit(): void {
    this.loadPage()
  }

  trackId(index: number, item: IVente): number {
    return item.id!;
  }

  delete(vente: IVente): void {
    const modalRef = this.modalService.open(VenteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vente = vente;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
  createOrUpdate(vente?: IVente):void {
    const modalRef = this.modalService.open(VenteUpdateComponent, { size: 'lg', backdrop: 'static' });
    if (vente){
      modalRef.componentInstance.vente = vente;
    }

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'succes') {
        this.loadPage();
      }
    });
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

  protected onSuccess(data: IVente[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/vente'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.ventes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
