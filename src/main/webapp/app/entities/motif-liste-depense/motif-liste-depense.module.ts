import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MotifListeDepenseComponent } from './list/motif-liste-depense.component';
import { MotifListeDepenseDetailComponent } from './detail/motif-liste-depense-detail.component';
import { MotifListeDepenseUpdateComponent } from './update/motif-liste-depense-update.component';
import { MotifListeDepenseDeleteDialogComponent } from './delete/motif-liste-depense-delete-dialog.component';
import { MotifListeDepenseRoutingModule } from './route/motif-liste-depense-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, MotifListeDepenseRoutingModule, NbCardModule, NbButtonModule, NbInputModule, MatButtonModule],
  declarations: [
    MotifListeDepenseComponent,
    MotifListeDepenseDetailComponent,
    MotifListeDepenseUpdateComponent,
    MotifListeDepenseDeleteDialogComponent,
  ],
  entryComponents: [MotifListeDepenseDeleteDialogComponent],
})
export class MotifListeDepenseModule {}
