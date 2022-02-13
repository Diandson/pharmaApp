import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MedicamentComponent } from './list/medicament.component';
import { MedicamentDetailComponent } from './detail/medicament-detail.component';
import { MedicamentUpdateComponent } from './update/medicament-update.component';
import { MedicamentDeleteDialogComponent } from './delete/medicament-delete-dialog.component';
import { MedicamentRoutingModule } from './route/medicament-routing.module';
import {NbButtonModule, NbCardModule} from "@nebular/theme";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {NzModalModule} from "ng-zorro-antd/modal";
import {NzUploadModule} from "ng-zorro-antd/upload";
import {MatButtonModule} from "@angular/material/button";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzIconModule} from "ng-zorro-antd/icon";

@NgModule({
  imports: [SharedModule, MedicamentRoutingModule, NbCardModule, NbButtonModule, TableModule, InputTextModule, NzModalModule, NzUploadModule, MatButtonModule, NzButtonModule, NzIconModule],
  declarations: [MedicamentComponent, MedicamentDetailComponent, MedicamentUpdateComponent, MedicamentDeleteDialogComponent],
  entryComponents: [MedicamentDeleteDialogComponent],
})
export class MedicamentModule {}
