import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VersementComponent } from './list/versement.component';
import { VersementDetailComponent } from './detail/versement-detail.component';
import { VersementUpdateComponent } from './update/versement-update.component';
import { VersementDeleteDialogComponent } from './delete/versement-delete-dialog.component';
import { VersementRoutingModule } from './route/versement-routing.module';

@NgModule({
  imports: [SharedModule, VersementRoutingModule],
  declarations: [VersementComponent, VersementDetailComponent, VersementUpdateComponent, VersementDeleteDialogComponent],
  entryComponents: [VersementDeleteDialogComponent],
})
export class VersementModule {}
