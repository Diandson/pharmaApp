import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';

@Component({
  templateUrl: './inventaire-delete-dialog.component.html',
})
export class InventaireDeleteDialogComponent {
  inventaire?: IInventaire;

  constructor(protected inventaireService: InventaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
