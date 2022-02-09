import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypePackComponent } from '../list/type-pack.component';
import { TypePackDetailComponent } from '../detail/type-pack-detail.component';
import { TypePackUpdateComponent } from '../update/type-pack-update.component';
import { TypePackRoutingResolveService } from './type-pack-routing-resolve.service';

const typePackRoute: Routes = [
  {
    path: '',
    component: TypePackComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypePackDetailComponent,
    resolve: {
      typePack: TypePackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypePackUpdateComponent,
    resolve: {
      typePack: TypePackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypePackUpdateComponent,
    resolve: {
      typePack: TypePackRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typePackRoute)],
  exports: [RouterModule],
})
export class TypePackRoutingModule {}
