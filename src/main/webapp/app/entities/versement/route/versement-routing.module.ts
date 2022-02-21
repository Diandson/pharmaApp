import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VersementComponent } from '../list/versement.component';
import { VersementDetailComponent } from '../detail/versement-detail.component';
import { VersementUpdateComponent } from '../update/versement-update.component';
import { VersementRoutingResolveService } from './versement-routing-resolve.service';

const versementRoute: Routes = [
  {
    path: '',
    component: VersementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VersementDetailComponent,
    resolve: {
      versement: VersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VersementUpdateComponent,
    resolve: {
      versement: VersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VersementUpdateComponent,
    resolve: {
      versement: VersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(versementRoute)],
  exports: [RouterModule],
})
export class VersementRoutingModule {}
