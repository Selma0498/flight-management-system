import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAirport } from 'app/shared/model/flights/airport.model';
import { AirportService } from './airport.service';
import { AirportDeleteDialogComponent } from './airport-delete-dialog.component';

@Component({
  selector: 'jhi-airport',
  templateUrl: './airport.component.html',
})
export class AirportComponent implements OnInit, OnDestroy {
  airports?: IAirport[];
  eventSubscriber?: Subscription;

  constructor(protected airportService: AirportService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.airportService.query().subscribe((res: HttpResponse<IAirport[]>) => (this.airports = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAirports();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAirport): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAirports(): void {
    this.eventSubscriber = this.eventManager.subscribe('airportListModification', () => this.loadAll());
  }

  delete(airport: IAirport): void {
    const modalRef = this.modalService.open(AirportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.airport = airport;
  }
}
