import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILieuVersement } from '../lieu-versement.model';
import { LieuVersementService } from '../service/lieu-versement.service';

@Component({
  templateUrl: './lieu-versement-delete-dialog.component.html',
})
export class LieuVersementDeleteDialogComponent {
  lieuVersement?: ILieuVersement;

  constructor(protected lieuVersementService: LieuVersementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lieuVersementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
