import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAirline } from 'app/shared/model/flights/airline.model';
import { AirlineService } from './airline.service';

@Component({
  templateUrl: './airline-delete-dialog.component.html',
})
export class AirlineDeleteDialogComponent {
  airline?: IAirline;

  constructor(protected airlineService: AirlineService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.airlineService.delete(id).subscribe(() => {
      this.eventManager.broadcast('airlineListModification');
      this.activeModal.close();
    });
  }
}
