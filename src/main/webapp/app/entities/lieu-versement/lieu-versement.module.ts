import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LieuVersementComponent } from './list/lieu-versement.component';
import { LieuVersementDetailComponent } from './detail/lieu-versement-detail.component';
import { LieuVersementUpdateComponent } from './update/lieu-versement-update.component';
import { LieuVersementDeleteDialogComponent } from './delete/lieu-versement-delete-dialog.component';
import { LieuVersementRoutingModule } from './route/lieu-versement-routing.module';

@NgModule({
  imports: [SharedModule, LieuVersementRoutingModule],
  declarations: [LieuVersementComponent, LieuVersementDetailComponent, LieuVersementUpdateComponent, LieuVersementDeleteDialogComponent],
  entryComponents: [LieuVersementDeleteDialogComponent],
})
export class LieuVersementModule {}
