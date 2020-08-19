import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILuggage } from 'app/shared/model/luggage/luggage.model';
import { LuggageService } from './luggage.service';

@Component({
  templateUrl: './luggage-delete-dialog.component.html',
})
export class LuggageDeleteDialogComponent {
  luggage?: ILuggage;

  constructor(protected luggageService: LuggageService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.luggageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('luggageListModification');
      this.activeModal.close();
    });
  }
}
