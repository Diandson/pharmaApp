import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypePack } from '../type-pack.model';
import { TypePackService } from '../service/type-pack.service';

@Component({
  templateUrl: './type-pack-delete-dialog.component.html',
})
export class TypePackDeleteDialogComponent {
  typePack?: ITypePack;

  constructor(protected typePackService: TypePackService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typePackService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
