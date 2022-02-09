import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVenteMedicament } from '../vente-medicament.model';
import { VenteMedicamentService } from '../service/vente-medicament.service';

@Component({
  templateUrl: './vente-medicament-delete-dialog.component.html',
})
export class VenteMedicamentDeleteDialogComponent {
  venteMedicament?: IVenteMedicament;

  constructor(protected venteMedicamentService: VenteMedicamentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.venteMedicamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
