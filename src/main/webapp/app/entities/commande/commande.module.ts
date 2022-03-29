import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommandeComponent } from './list/commande.component';
import { CommandeDetailComponent } from './detail/commande-detail.component';
import { CommandeUpdateComponent } from './update/commande-update.component';
import { CommandeDeleteDialogComponent } from './delete/commande-delete-dialog.component';
import { CommandeRoutingModule } from './route/commande-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {MatButtonModule} from "@angular/material/button";
import {InputNumberModule} from "primeng/inputnumber";
import {InputMaskModule} from "@ngneat/input-mask";
import {NzModalModule} from "ng-zorro-antd/modal";
import {NzSelectModule} from "ng-zorro-antd/select";
import { GenererComponent } from './generer/generer.component';

@NgModule({
  imports: [SharedModule, CommandeRoutingModule, NbButtonModule, NbCardModule, TableModule, InputTextModule, MatButtonModule, InputNumberModule, InputMaskModule, NzModalModule, NbInputModule, NzSelectModule],
  declarations: [CommandeComponent, CommandeDetailComponent, CommandeUpdateComponent, CommandeDeleteDialogComponent, GenererComponent],
  entryComponents: [CommandeDeleteDialogComponent],
})
export class CommandeModule {}
