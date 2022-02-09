import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypePackComponent } from './list/type-pack.component';
import { TypePackDetailComponent } from './detail/type-pack-detail.component';
import { TypePackUpdateComponent } from './update/type-pack-update.component';
import { TypePackDeleteDialogComponent } from './delete/type-pack-delete-dialog.component';
import { TypePackRoutingModule } from './route/type-pack-routing.module';

@NgModule({
  imports: [SharedModule, TypePackRoutingModule],
  declarations: [TypePackComponent, TypePackDetailComponent, TypePackUpdateComponent, TypePackDeleteDialogComponent],
  entryComponents: [TypePackDeleteDialogComponent],
})
export class TypePackModule {}
