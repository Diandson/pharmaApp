import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VenteMedicamentComponent } from '../list/vente-medicament.component';
import { VenteMedicamentDetailComponent } from '../detail/vente-medicament-detail.component';
import { VenteMedicamentUpdateComponent } from '../update/vente-medicament-update.component';
import { VenteMedicamentRoutingResolveService } from './vente-medicament-routing-resolve.service';

const venteMedicamentRoute: Routes = [
  {
    path: '',
    component: VenteMedicamentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VenteMedicamentDetailComponent,
    resolve: {
      venteMedicament: VenteMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VenteMedicamentUpdateComponent,
    resolve: {
      venteMedicament: VenteMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VenteMedicamentUpdateComponent,
    resolve: {
      venteMedicament: VenteMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(venteMedicamentRoute)],
  exports: [RouterModule],
})
export class VenteMedicamentRoutingModule {}
