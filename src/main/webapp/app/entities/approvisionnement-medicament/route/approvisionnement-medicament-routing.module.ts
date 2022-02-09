import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovisionnementMedicamentComponent } from '../list/approvisionnement-medicament.component';
import { ApprovisionnementMedicamentDetailComponent } from '../detail/approvisionnement-medicament-detail.component';
import { ApprovisionnementMedicamentUpdateComponent } from '../update/approvisionnement-medicament-update.component';
import { ApprovisionnementMedicamentRoutingResolveService } from './approvisionnement-medicament-routing-resolve.service';

const approvisionnementMedicamentRoute: Routes = [
  {
    path: '',
    component: ApprovisionnementMedicamentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovisionnementMedicamentDetailComponent,
    resolve: {
      approvisionnementMedicament: ApprovisionnementMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovisionnementMedicamentUpdateComponent,
    resolve: {
      approvisionnementMedicament: ApprovisionnementMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovisionnementMedicamentUpdateComponent,
    resolve: {
      approvisionnementMedicament: ApprovisionnementMedicamentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvisionnementMedicamentRoute)],
  exports: [RouterModule],
})
export class ApprovisionnementMedicamentRoutingModule {}
