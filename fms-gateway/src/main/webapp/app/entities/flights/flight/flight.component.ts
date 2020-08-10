import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlight } from 'app/shared/model/flights/flight.model';
import { FlightService } from './flight.service';
import { FlightDeleteDialogComponent } from './flight-delete-dialog.component';

@Component({
  selector: 'jhi-flight',
  templateUrl: './flight.component.html',
})
export class FlightComponent implements OnInit, OnDestroy {
  flights?: IFlight[];
  eventSubscriber?: Subscription;

  constructor(protected flightService: FlightService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.flightService.query().subscribe((res: HttpResponse<IFlight[]>) => (this.flights = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFlights();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFlight): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFlights(): void {
    this.eventSubscriber = this.eventManager.subscribe('flightListModification', () => this.loadAll());
  }

  delete(flight: IFlight): void {
    const modalRef = this.modalService.open(FlightDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.flight = flight;
  }
}
