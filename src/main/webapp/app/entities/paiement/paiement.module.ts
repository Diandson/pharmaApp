import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaiementComponent } from './list/paiement.component';
import { PaiementDetailComponent } from './detail/paiement-detail.component';
import { PaiementUpdateComponent } from './update/paiement-update.component';
import { PaiementDeleteDialogComponent } from './delete/paiement-delete-dialog.component';
import { PaiementRoutingModule } from './route/paiement-routing.module';
import {MatButtonModule} from "@angular/material/button";
import {NbInputModule} from "@nebular/theme";
import {InputMaskModule} from "@ngneat/input-mask";

@NgModule({
    imports: [
      SharedModule, PaiementRoutingModule,
      MatButtonModule, NbInputModule, InputMaskModule.forRoot({ inputSelector: 'input', isAsync: true })
    ],
  declarations: [PaiementComponent, PaiementDetailComponent, PaiementUpdateComponent, PaiementDeleteDialogComponent],
  entryComponents: [PaiementDeleteDialogComponent],
})
export class PaiementModule {}
