import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { FlightHandlingService } from './flight-handling.service';
import { FlightHandlingDeleteDialogComponent } from './flight-handling-delete-dialog.component';

@Component({
  selector: 'jhi-flight-handling',
  templateUrl: './flight-handling.component.html',
})
export class FlightHandlingComponent implements OnInit, OnDestroy {
  flightHandlings?: IFlightHandling[];
  eventSubscriber?: Subscription;

  constructor(
    protected flightHandlingService: FlightHandlingService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.flightHandlingService.query().subscribe((res: HttpResponse<IFlightHandling[]>) => (this.flightHandlings = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFlightHandlings();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFlightHandling): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFlightHandlings(): void {
    this.eventSubscriber = this.eventManager.subscribe('flightHandlingListModification', () => this.loadAll());
  }

  delete(flightHandling: IFlightHandling): void {
    const modalRef = this.modalService.open(FlightHandlingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.flightHandling = flightHandling;
  }
}
