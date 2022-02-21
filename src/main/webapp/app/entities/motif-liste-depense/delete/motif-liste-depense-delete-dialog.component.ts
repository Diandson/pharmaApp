import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMotifListeDepense } from '../motif-liste-depense.model';
import { MotifListeDepenseService } from '../service/motif-liste-depense.service';

@Component({
  templateUrl: './motif-liste-depense-delete-dialog.component.html',
})
export class MotifListeDepenseDeleteDialogComponent {
  motifListeDepense?: IMotifListeDepense;

  constructor(protected motifListeDepenseService: MotifListeDepenseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.motifListeDepenseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
