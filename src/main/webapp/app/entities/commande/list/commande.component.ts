import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommande } from '../commande.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { CommandeService } from '../service/commande.service';
import { CommandeDeleteDialogComponent } from '../delete/commande-delete-dialog.component';
import {CommandeDetailComponent} from "../detail/commande-detail.component";
import {CommandeUpdateComponent} from "../update/commande-update.component";

@Component({
  selector: 'jhi-commande',
  templateUrl: './commande.component.html',
})
export class CommandeComponent implements OnInit {
  commandes?: ICommande[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  search?: string;

  constructor(
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {

    this.commandeService
      .query()
      .subscribe(res => this.commandes = res.body ?? []);
  }

  ngOnInit(): void {
    // this.handleNavigation();
    this.loadPage();
  }

  trackId(index: number, item: ICommande): number {
    return item.id!;
  }

  delete(commande: ICommande): void {
    const modalRef = this.modalService.open(CommandeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commande = commande;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  createOrUpdate(commande?: ICommande): void {
    const modalRef = this.modalService.open(CommandeUpdateComponent, { size: 'xl', backdrop: 'static' });
    if (commande){
      modalRef.componentInstance.commande = commande;
    }

    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'succes') {
        this.loadPage();
      }
    });
  }

  detailApproviso(commande?: ICommande): void {
    const modalRef = this.modalService.open(CommandeDetailComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.commande = commande;
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

  protected onSuccess(data: ICommande[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/commande'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.commandes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
