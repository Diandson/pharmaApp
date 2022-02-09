import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovisionnement } from '../approvisionnement.model';
import { ApprovisionnementService } from '../service/approvisionnement.service';

@Component({
  templateUrl: './approvisionnement-delete-dialog.component.html',
})
export class ApprovisionnementDeleteDialogComponent {
  approvisionnement?: IApprovisionnement;

  constructor(protected approvisionnementService: ApprovisionnementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvisionnementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
