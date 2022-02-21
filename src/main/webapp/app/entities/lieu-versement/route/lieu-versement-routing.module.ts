import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LieuVersementComponent } from '../list/lieu-versement.component';
import { LieuVersementDetailComponent } from '../detail/lieu-versement-detail.component';
import { LieuVersementUpdateComponent } from '../update/lieu-versement-update.component';
import { LieuVersementRoutingResolveService } from './lieu-versement-routing-resolve.service';

const lieuVersementRoute: Routes = [
  {
    path: '',
    component: LieuVersementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LieuVersementDetailComponent,
    resolve: {
      lieuVersement: LieuVersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LieuVersementUpdateComponent,
    resolve: {
      lieuVersement: LieuVersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LieuVersementUpdateComponent,
    resolve: {
      lieuVersement: LieuVersementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lieuVersementRoute)],
  exports: [RouterModule],
})
export class LieuVersementRoutingModule {}
