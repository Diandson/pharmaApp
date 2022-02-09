import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventaireComponent } from '../list/inventaire.component';
import { InventaireDetailComponent } from '../detail/inventaire-detail.component';
import { InventaireUpdateComponent } from '../update/inventaire-update.component';
import { InventaireRoutingResolveService } from './inventaire-routing-resolve.service';

const inventaireRoute: Routes = [
  {
    path: '',
    component: InventaireComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventaireDetailComponent,
    resolve: {
      inventaire: InventaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventaireUpdateComponent,
    resolve: {
      inventaire: InventaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventaireUpdateComponent,
    resolve: {
      inventaire: InventaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inventaireRoute)],
  exports: [RouterModule],
})
export class InventaireRoutingModule {}
