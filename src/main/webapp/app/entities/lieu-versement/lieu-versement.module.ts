import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LieuVersementComponent } from './list/lieu-versement.component';
import { LieuVersementDetailComponent } from './detail/lieu-versement-detail.component';
import { LieuVersementUpdateComponent } from './update/lieu-versement-update.component';
import { LieuVersementDeleteDialogComponent } from './delete/lieu-versement-delete-dialog.component';
import { LieuVersementRoutingModule } from './route/lieu-versement-routing.module';
import {NbButtonModule, NbCardModule, NbInputModule} from "@nebular/theme";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [SharedModule, LieuVersementRoutingModule, NbButtonModule, NbCardModule, NbInputModule, MatButtonModule],
  declarations: [LieuVersementComponent, LieuVersementDetailComponent, LieuVersementUpdateComponent, LieuVersementDeleteDialogComponent],
  entryComponents: [LieuVersementDeleteDialogComponent],
})
export class LieuVersementModule {}
