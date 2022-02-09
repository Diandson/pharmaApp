import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventaireMedicamentComponent } from '../list/inventaire-medicament.component';
import { InventaireMedicamentDetailComponent } from '../detail/inventaire-medicament-detail.component';
import { InventaireMedicamentUpdateComponent } from '../update/inventaire-medicament-update.component';
import { InventaireMedicamentRoutingResolveService } from './inventaire-medicament-routing-resolve.service';

const inventaireMedicamentRoute: Routes = [
  {
    path: '',
    component: InventaireMedicamentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventaireMedicamentDetailComponent,
    resolve: {
      inventaireMedicament: InventaireMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventaireMedicamentUpdateComponent,
    resolve: {
      inventaireMedicament: InventaireMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventaireMedicamentUpdateComponent,
    resolve: {
      inventaireMedicament: InventaireMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inventaireMedicamentRoute)],
  exports: [RouterModule],
})
export class InventaireMedicamentRoutingModule {}
