import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAirport } from 'app/shared/model/flights/airport.model';
import { AirportService } from './airport.service';

@Component({
  templateUrl: './airport-delete-dialog.component.html',
})
export class AirportDeleteDialogComponent {
  airport?: IAirport;

  constructor(protected airportService: AirportService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.airportService.delete(id).subscribe(() => {
      this.eventManager.broadcast('airportListModification');
      this.activeModal.close();
    });
  }
}
