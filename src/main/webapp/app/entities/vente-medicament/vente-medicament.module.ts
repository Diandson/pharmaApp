import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VenteMedicamentComponent } from './list/vente-medicament.component';
import { VenteMedicamentDetailComponent } from './detail/vente-medicament-detail.component';
import { VenteMedicamentUpdateComponent } from './update/vente-medicament-update.component';
import { VenteMedicamentDeleteDialogComponent } from './delete/vente-medicament-delete-dialog.component';
import { VenteMedicamentRoutingModule } from './route/vente-medicament-routing.module';

@NgModule({
  imports: [SharedModule, VenteMedicamentRoutingModule],
  declarations: [
    VenteMedicamentComponent,
    VenteMedicamentDetailComponent,
    VenteMedicamentUpdateComponent,
    VenteMedicamentDeleteDialogComponent,
  ],
  entryComponents: [VenteMedicamentDeleteDialogComponent],
})
export class VenteMedicamentModule {}
