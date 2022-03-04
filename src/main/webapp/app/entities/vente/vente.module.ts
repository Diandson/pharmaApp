import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VenteComponent } from './list/vente.component';
import { VenteDetailComponent } from './detail/vente-detail.component';
import { VenteUpdateComponent } from './update/vente-update.component';
import { VenteDeleteDialogComponent } from './delete/vente-delete-dialog.component';
import { VenteRoutingModule } from './route/vente-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";

@NgModule({
  imports: [SharedModule, VenteRoutingModule, NbButtonModule, NbCardModule, MatButtonModule, NbInputModule, TableModule, InputTextModule],
  declarations: [VenteComponent, VenteDetailComponent, VenteUpdateComponent, VenteDeleteDialogComponent],
  entryComponents: [VenteDeleteDialogComponent],
})
export class VenteModule {}
