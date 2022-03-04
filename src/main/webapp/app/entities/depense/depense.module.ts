import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepenseComponent } from './list/depense.component';
import { DepenseDetailComponent } from './detail/depense-detail.component';
import { DepenseUpdateComponent } from './update/depense-update.component';
import { DepenseDeleteDialogComponent } from './delete/depense-delete-dialog.component';
import { DepenseRoutingModule } from './route/depense-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";

@NgModule({
  imports: [SharedModule, DepenseRoutingModule, NbInputModule, MatButtonModule, NbCardModule, TableModule, InputTextModule, NbButtonModule],
  declarations: [DepenseComponent, DepenseDetailComponent, DepenseUpdateComponent, DepenseDeleteDialogComponent],
  entryComponents: [DepenseDeleteDialogComponent],
})
export class DepenseModule {}
