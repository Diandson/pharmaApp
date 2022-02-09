import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InventaireComponent } from './list/inventaire.component';
import { InventaireDetailComponent } from './detail/inventaire-detail.component';
import { InventaireUpdateComponent } from './update/inventaire-update.component';
import { InventaireDeleteDialogComponent } from './delete/inventaire-delete-dialog.component';
import { InventaireRoutingModule } from './route/inventaire-routing.module';

@NgModule({
  imports: [SharedModule, InventaireRoutingModule],
  declarations: [InventaireComponent, InventaireDetailComponent, InventaireUpdateComponent, InventaireDeleteDialogComponent],
  entryComponents: [InventaireDeleteDialogComponent],
})
export class InventaireModule {}
