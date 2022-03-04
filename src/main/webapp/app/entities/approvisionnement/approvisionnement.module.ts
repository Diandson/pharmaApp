import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApprovisionnementComponent } from './list/approvisionnement.component';
import { ApprovisionnementDetailComponent } from './detail/approvisionnement-detail.component';
import { ApprovisionnementUpdateComponent } from './update/approvisionnement-update.component';
import { ApprovisionnementDeleteDialogComponent } from './delete/approvisionnement-delete-dialog.component';
import { ApprovisionnementRoutingModule } from './route/approvisionnement-routing.module';
import {NbButtonModule, NbCardModule} from "@nebular/theme";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, ApprovisionnementRoutingModule, NbButtonModule, NbCardModule, TableModule, InputTextModule, MatButtonModule],
  declarations: [
    ApprovisionnementComponent,
    ApprovisionnementDetailComponent,
    ApprovisionnementUpdateComponent,
    ApprovisionnementDeleteDialogComponent,
  ],
  entryComponents: [ApprovisionnementDeleteDialogComponent],
})
export class ApprovisionnementModule {}
