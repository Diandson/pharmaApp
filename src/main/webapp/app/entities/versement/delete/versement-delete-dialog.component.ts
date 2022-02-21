import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVersement } from '../versement.model';
import { VersementService } from '../service/versement.service';

@Component({
  templateUrl: './versement-delete-dialog.component.html',
})
export class VersementDeleteDialogComponent {
  versement?: IVersement;

  constructor(protected versementService: VersementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.versementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
