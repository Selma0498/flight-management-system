import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { FlightHandlingService } from './flight-handling.service';

@Component({
  templateUrl: './flight-handling-delete-dialog.component.html',
})
export class FlightHandlingDeleteDialogComponent {
  flightHandling?: IFlightHandling;

  constructor(
    protected flightHandlingService: FlightHandlingService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flightHandlingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('flightHandlingListModification');
      this.activeModal.close();
    });
  }
}
