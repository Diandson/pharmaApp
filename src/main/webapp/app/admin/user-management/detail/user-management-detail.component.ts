import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from '../user-management.model';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
})
export class UserManagementDetailComponent implements OnInit {
  user: User | null = null;
  str?: string

  constructor(
    private route: ActivatedRoute,
    private activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    // this.route.data.subscribe(({ user }) => {
    //   this.user = user;
    // });
    this.str = 'jnei';
  }

  close(): void {
    this.activeModal.close();
  }
}
