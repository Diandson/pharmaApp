import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommandeMedicament } from '../commande-medicament.model';
import { CommandeMedicamentService } from '../service/commande-medicament.service';

@Component({
  templateUrl: './commande-medicament-delete-dialog.component.html',
})
export class CommandeMedicamentDeleteDialogComponent {
  commandeMedicament?: ICommandeMedicament;

  constructor(protected commandeMedicamentService: CommandeMedicamentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandeMedicamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
