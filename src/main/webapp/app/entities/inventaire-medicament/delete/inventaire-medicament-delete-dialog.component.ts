import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventaireMedicament } from '../inventaire-medicament.model';
import { InventaireMedicamentService } from '../service/inventaire-medicament.service';

@Component({
  templateUrl: './inventaire-medicament-delete-dialog.component.html',
})
export class InventaireMedicamentDeleteDialogComponent {
  inventaireMedicament?: IInventaireMedicament;

  constructor(protected inventaireMedicamentService: InventaireMedicamentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventaireMedicamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
