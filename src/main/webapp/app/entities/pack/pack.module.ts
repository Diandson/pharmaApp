import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PackComponent } from './list/pack.component';
import { PackDetailComponent } from './detail/pack-detail.component';
import { PackUpdateComponent } from './update/pack-update.component';
import { PackDeleteDialogComponent } from './delete/pack-delete-dialog.component';
import { PackRoutingModule } from './route/pack-routing.module';
import {NbCardModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";
import {NzSelectModule} from "ng-zorro-antd/select";

@NgModule({
  imports: [SharedModule, PackRoutingModule, NbCardModule, MatButtonModule, NzSelectModule],
  declarations: [PackComponent, PackDetailComponent, PackUpdateComponent, PackDeleteDialogComponent],
  entryComponents: [PackDeleteDialogComponent],
})
export class PackModule {}
