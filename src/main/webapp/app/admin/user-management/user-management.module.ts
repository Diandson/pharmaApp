import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { UserManagementDetailComponent } from './detail/user-management-detail.component';
import { UserManagementUpdateComponent } from './update/user-management-update.component';
import { UserManagementDeleteDialogComponent } from './delete/user-management-delete-dialog.component';
import { userManagementRoute } from './user-management.route';
import {
    NbButtonGroupModule,
    NbButtonModule,
    NbCardModule,
    NbIconModule,
    NbInputModule,
    NbTreeGridModule
} from "@nebular/theme";
import {UserManagementComponent} from "./list/user-management.component";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MatButtonModule} from "@angular/material/button";
import {AccountModule} from "../../account/account.module";
import {InputMaskModule} from "@ngneat/input-mask";

@NgModule({
    imports: [SharedModule, RouterModule.forChild(userManagementRoute), NbCardModule, NbInputModule, NbTreeGridModule, NbButtonModule, NbIconModule, TableModule, ButtonModule, InputTextModule, MatButtonModule, AccountModule, InputMaskModule, NbButtonGroupModule],
  declarations: [
    UserManagementComponent,
    UserManagementDetailComponent,
    UserManagementUpdateComponent,
    UserManagementDeleteDialogComponent,
  ],
  entryComponents: [UserManagementDeleteDialogComponent],
})
export class UserManagementModule {}
