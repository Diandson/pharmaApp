import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPack } from '../pack.model';
import { PackService } from '../service/pack.service';

@Component({
  templateUrl: './pack-delete-dialog.component.html',
})
export class PackDeleteDialogComponent {
  pack?: IPack;

  constructor(protected packService: PackService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.packService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
