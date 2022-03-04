import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VersementComponent } from './list/versement.component';
import { VersementDetailComponent } from './detail/versement-detail.component';
import { VersementUpdateComponent } from './update/versement-update.component';
import { VersementDeleteDialogComponent } from './delete/versement-delete-dialog.component';
import { VersementRoutingModule } from './route/versement-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, VersementRoutingModule, NbButtonModule, NbCardModule, TableModule, InputTextModule, MatButtonModule, NbInputModule],
  declarations: [VersementComponent, VersementDetailComponent, VersementUpdateComponent, VersementDeleteDialogComponent],
  entryComponents: [VersementDeleteDialogComponent],
})
export class VersementModule {}
