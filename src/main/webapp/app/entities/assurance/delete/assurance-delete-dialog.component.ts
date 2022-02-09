import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssurance } from '../assurance.model';
import { AssuranceService } from '../service/assurance.service';

@Component({
  templateUrl: './assurance-delete-dialog.component.html',
})
export class AssuranceDeleteDialogComponent {
  assurance?: IAssurance;

  constructor(protected assuranceService: AssuranceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assuranceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
