import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StructureComponent } from './list/structure.component';
import { StructureDetailComponent } from './detail/structure-detail.component';
import { StructureUpdateComponent } from './update/structure-update.component';
import { StructureDeleteDialogComponent } from './delete/structure-delete-dialog.component';
import { StructureRoutingModule } from './route/structure-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule, NbSelectModule, NbStepperModule} from "@nebular/theme";
import {NzInputModule} from "ng-zorro-antd/input";
import {NzSelectModule} from "ng-zorro-antd/select";
import {NzInputNumberModule} from "ng-zorro-antd/input-number";
import {NzUploadModule} from "ng-zorro-antd/upload";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzMessageService} from "ng-zorro-antd/message";
import {MatButtonModule} from "@angular/material/button";
import {AccountModule} from "../../account/account.module";
import {InputMaskModule} from "@ngneat/input-mask";

@NgModule({
  imports: [SharedModule, StructureRoutingModule, NbCardModule,
    NbStepperModule, NbButtonModule, NzInputModule, NzSelectModule,
    NzInputNumberModule, NzUploadModule, NzIconModule, NzButtonModule,
    MatButtonModule, AccountModule, InputMaskModule, InputMaskModule, NbInputModule, NbSelectModule
  ],
  declarations: [StructureComponent, StructureDetailComponent, StructureUpdateComponent, StructureDeleteDialogComponent],
  providers: [
    NzMessageService
  ],
  entryComponents: [StructureDeleteDialogComponent],
})
export class StructureModule {}
