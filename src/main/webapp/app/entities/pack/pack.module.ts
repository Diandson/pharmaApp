import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PackComponent } from './list/pack.component';
import { PackDetailComponent } from './detail/pack-detail.component';
import { PackUpdateComponent } from './update/pack-update.component';
import { PackDeleteDialogComponent } from './delete/pack-delete-dialog.component';
import { PackRoutingModule } from './route/pack-routing.module';

@NgModule({
  imports: [SharedModule, PackRoutingModule],
  declarations: [PackComponent, PackDetailComponent, PackUpdateComponent, PackDeleteDialogComponent],
  entryComponents: [PackDeleteDialogComponent],
})
export class PackModule {}
