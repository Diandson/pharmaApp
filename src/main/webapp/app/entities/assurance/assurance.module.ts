import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssuranceComponent } from './list/assurance.component';
import { AssuranceDetailComponent } from './detail/assurance-detail.component';
import { AssuranceUpdateComponent } from './update/assurance-update.component';
import { AssuranceDeleteDialogComponent } from './delete/assurance-delete-dialog.component';
import { AssuranceRoutingModule } from './route/assurance-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, AssuranceRoutingModule, NbCardModule, NbButtonModule, NbInputModule, MatButtonModule],
  declarations: [AssuranceComponent, AssuranceDetailComponent, AssuranceUpdateComponent, AssuranceDeleteDialogComponent],
  entryComponents: [AssuranceDeleteDialogComponent],
})
export class AssuranceModule {}
