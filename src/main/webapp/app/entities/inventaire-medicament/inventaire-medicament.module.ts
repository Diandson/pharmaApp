import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InventaireMedicamentComponent } from './list/inventaire-medicament.component';
import { InventaireMedicamentDetailComponent } from './detail/inventaire-medicament-detail.component';
import { InventaireMedicamentUpdateComponent } from './update/inventaire-medicament-update.component';
import { InventaireMedicamentDeleteDialogComponent } from './delete/inventaire-medicament-delete-dialog.component';
import { InventaireMedicamentRoutingModule } from './route/inventaire-medicament-routing.module';

@NgModule({
  imports: [SharedModule, InventaireMedicamentRoutingModule],
  declarations: [
    InventaireMedicamentComponent,
    InventaireMedicamentDetailComponent,
    InventaireMedicamentUpdateComponent,
    InventaireMedicamentDeleteDialogComponent,
  ],
  entryComponents: [InventaireMedicamentDeleteDialogComponent],
})
export class InventaireMedicamentModule {}
