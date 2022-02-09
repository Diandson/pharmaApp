import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovisionnementComponent } from '../list/approvisionnement.component';
import { ApprovisionnementDetailComponent } from '../detail/approvisionnement-detail.component';
import { ApprovisionnementUpdateComponent } from '../update/approvisionnement-update.component';
import { ApprovisionnementRoutingResolveService } from './approvisionnement-routing-resolve.service';

const approvisionnementRoute: Routes = [
  {
    path: '',
    component: ApprovisionnementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovisionnementDetailComponent,
    resolve: {
      approvisionnement: ApprovisionnementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovisionnementUpdateComponent,
    resolve: {
      approvisionnement: ApprovisionnementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovisionnementUpdateComponent,
    resolve: {
      approvisionnement: ApprovisionnementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvisionnementRoute)],
  exports: [RouterModule],
})
export class ApprovisionnementRoutingModule {}
