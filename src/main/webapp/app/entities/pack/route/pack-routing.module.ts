import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PackComponent } from '../list/pack.component';
import { PackDetailComponent } from '../detail/pack-detail.component';
import { PackUpdateComponent } from '../update/pack-update.component';
import { PackRoutingResolveService } from './pack-routing-resolve.service';

const packRoute: Routes = [
  {
    path: '',
    component: PackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PackDetailComponent,
    resolve: {
      pack: PackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PackUpdateComponent,
    resolve: {
      pack: PackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PackUpdateComponent,
    resolve: {
      pack: PackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(packRoute)],
  exports: [RouterModule],
})
export class PackRoutingModule {}
