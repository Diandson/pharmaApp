import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssuranceComponent } from './list/assurance.component';
import { AssuranceDetailComponent } from './detail/assurance-detail.component';
import { AssuranceUpdateComponent } from './update/assurance-update.component';
import { AssuranceDeleteDialogComponent } from './delete/assurance-delete-dialog.component';
import { AssuranceRoutingModule } from './route/assurance-routing.module';

@NgModule({
  imports: [SharedModule, AssuranceRoutingModule],
  declarations: [AssuranceComponent, AssuranceDetailComponent, AssuranceUpdateComponent, AssuranceDeleteDialogComponent],
  entryComponents: [AssuranceDeleteDialogComponent],
})
export class AssuranceModule {}
