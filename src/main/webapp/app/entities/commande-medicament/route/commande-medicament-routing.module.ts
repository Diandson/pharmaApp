import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommandeMedicamentComponent } from '../list/commande-medicament.component';
import { CommandeMedicamentDetailComponent } from '../detail/commande-medicament-detail.component';
import { CommandeMedicamentUpdateComponent } from '../update/commande-medicament-update.component';
import { CommandeMedicamentRoutingResolveService } from './commande-medicament-routing-resolve.service';

const commandeMedicamentRoute: Routes = [
  {
    path: '',
    component: CommandeMedicamentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommandeMedicamentDetailComponent,
    resolve: {
      commandeMedicament: CommandeMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommandeMedicamentUpdateComponent,
    resolve: {
      commandeMedicament: CommandeMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommandeMedicamentUpdateComponent,
    resolve: {
      commandeMedicament: CommandeMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commandeMedicamentRoute)],
  exports: [RouterModule],
})
export class CommandeMedicamentRoutingModule {}
