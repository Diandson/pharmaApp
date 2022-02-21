import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MotifListeDepenseComponent } from '../list/motif-liste-depense.component';
import { MotifListeDepenseDetailComponent } from '../detail/motif-liste-depense-detail.component';
import { MotifListeDepenseUpdateComponent } from '../update/motif-liste-depense-update.component';
import { MotifListeDepenseRoutingResolveService } from './motif-liste-depense-routing-resolve.service';

const motifListeDepenseRoute: Routes = [
  {
    path: '',
    component: MotifListeDepenseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MotifListeDepenseDetailComponent,
    resolve: {
      motifListeDepense: MotifListeDepenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MotifListeDepenseUpdateComponent,
    resolve: {
      motifListeDepense: MotifListeDepenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MotifListeDepenseUpdateComponent,
    resolve: {
      motifListeDepense: MotifListeDepenseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(motifListeDepenseRoute)],
  exports: [RouterModule],
})
export class MotifListeDepenseRoutingModule {}
