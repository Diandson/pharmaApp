import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommandeMedicamentComponent } from './list/commande-medicament.component';
import { CommandeMedicamentDetailComponent } from './detail/commande-medicament-detail.component';
import { CommandeMedicamentUpdateComponent } from './update/commande-medicament-update.component';
import { CommandeMedicamentDeleteDialogComponent } from './delete/commande-medicament-delete-dialog.component';
import { CommandeMedicamentRoutingModule } from './route/commande-medicament-routing.module';

@NgModule({
  imports: [SharedModule, CommandeMedicamentRoutingModule],
  declarations: [
    CommandeMedicamentComponent,
    CommandeMedicamentDetailComponent,
    CommandeMedicamentUpdateComponent,
    CommandeMedicamentDeleteDialogComponent,
  ],
  entryComponents: [CommandeMedicamentDeleteDialogComponent],
})
export class CommandeMedicamentModule {}
