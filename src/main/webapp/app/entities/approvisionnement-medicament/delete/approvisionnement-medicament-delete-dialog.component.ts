import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovisionnementMedicament } from '../approvisionnement-medicament.model';
import { ApprovisionnementMedicamentService } from '../service/approvisionnement-medicament.service';

@Component({
  templateUrl: './approvisionnement-medicament-delete-dialog.component.html',
})
export class ApprovisionnementMedicamentDeleteDialogComponent {
  approvisionnementMedicament?: IApprovisionnementMedicament;

  constructor(protected approvisionnementMedicamentService: ApprovisionnementMedicamentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvisionnementMedicamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
