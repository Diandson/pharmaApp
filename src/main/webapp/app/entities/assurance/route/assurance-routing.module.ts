import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssuranceComponent } from '../list/assurance.component';
import { AssuranceDetailComponent } from '../detail/assurance-detail.component';
import { AssuranceUpdateComponent } from '../update/assurance-update.component';
import { AssuranceRoutingResolveService } from './assurance-routing-resolve.service';

const assuranceRoute: Routes = [
  {
    path: '',
    component: AssuranceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssuranceDetailComponent,
    resolve: {
      assurance: AssuranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssuranceUpdateComponent,
    resolve: {
      assurance: AssuranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssuranceUpdateComponent,
    resolve: {
      assurance: AssuranceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assuranceRoute)],
  exports: [RouterModule],
})
export class AssuranceRoutingModule {}
