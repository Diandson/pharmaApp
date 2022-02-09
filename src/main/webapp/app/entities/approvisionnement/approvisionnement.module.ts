import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApprovisionnementComponent } from './list/approvisionnement.component';
import { ApprovisionnementDetailComponent } from './detail/approvisionnement-detail.component';
import { ApprovisionnementUpdateComponent } from './update/approvisionnement-update.component';
import { ApprovisionnementDeleteDialogComponent } from './delete/approvisionnement-delete-dialog.component';
import { ApprovisionnementRoutingModule } from './route/approvisionnement-routing.module';

@NgModule({
  imports: [SharedModule, ApprovisionnementRoutingModule],
  declarations: [
    ApprovisionnementComponent,
    ApprovisionnementDetailComponent,
    ApprovisionnementUpdateComponent,
    ApprovisionnementDeleteDialogComponent,
  ],
  entryComponents: [ApprovisionnementDeleteDialogComponent],
})
export class ApprovisionnementModule {}
