import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApprovisionnementMedicamentComponent } from './list/approvisionnement-medicament.component';
import { ApprovisionnementMedicamentDetailComponent } from './detail/approvisionnement-medicament-detail.component';
import { ApprovisionnementMedicamentUpdateComponent } from './update/approvisionnement-medicament-update.component';
import { ApprovisionnementMedicamentDeleteDialogComponent } from './delete/approvisionnement-medicament-delete-dialog.component';
import { ApprovisionnementMedicamentRoutingModule } from './route/approvisionnement-medicament-routing.module';

@NgModule({
  imports: [SharedModule, ApprovisionnementMedicamentRoutingModule],
  declarations: [
    ApprovisionnementMedicamentComponent,
    ApprovisionnementMedicamentDetailComponent,
    ApprovisionnementMedicamentUpdateComponent,
    ApprovisionnementMedicamentDeleteDialogComponent,
  ],
  entryComponents: [ApprovisionnementMedicamentDeleteDialogComponent],
})
export class ApprovisionnementMedicamentModule {}
