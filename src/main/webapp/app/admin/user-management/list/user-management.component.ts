import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import { UserManagementDeleteDialogComponent } from '../delete/user-management-delete-dialog.component';
import {UserManagementUpdateComponent} from "../update/user-management-update.component";
import {UserManagementDetailComponent} from "../detail/user-management-detail.component";


@Component({
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent implements OnInit {
  currentAccount: Account | null = null;
  users: User[] | null = null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  search?: string;

  constructor(
    private userService: UserManagementService,
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    // this.handleNavigation();
    this.loadAll();
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(index: number, item: User): number {
    return item.id!;
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
  createOrUpdateUser(user?: User): void {
    const modalRef = this.modalService.open(UserManagementUpdateComponent, { size: 'xl', backdrop: 'static' });
    if (user){
      modalRef.componentInstance.user = user;
    }
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'saved') {
        this.loadAll();
      }
    });
  }
  detailsUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDetailComponent,
      { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'saved') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading = true;
    this.userService.query().subscribe(res => {
      this.users = res.body
      this.isLoading = false;
    });
  }

  transition(): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: `${this.predicate},${this.ascending ? ASC : DESC}`,
      },
    });
  }

  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      this.predicate = sort[0];
      this.ascending = sort[1] === ASC;
      this.loadAll();
    });
  }

  private sort(): string[] {
    const result = [`${this.predicate},${this.ascending ? ASC : DESC}`];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(users: User[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.users = users;
  }
}
