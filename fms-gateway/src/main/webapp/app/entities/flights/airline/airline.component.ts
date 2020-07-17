import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAirline } from 'app/shared/model/flights/airline.model';
import { AirlineService } from './airline.service';
import { AirlineDeleteDialogComponent } from './airline-delete-dialog.component';

@Component({
  selector: 'jhi-airline',
  templateUrl: './airline.component.html',
})
export class AirlineComponent implements OnInit, OnDestroy {
  airlines?: IAirline[];
  eventSubscriber?: Subscription;

  constructor(protected airlineService: AirlineService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.airlineService.query().subscribe((res: HttpResponse<IAirline[]>) => (this.airlines = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAirlines();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAirline): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAirlines(): void {
    this.eventSubscriber = this.eventManager.subscribe('airlineListModification', () => this.loadAll());
  }

  delete(airline: IAirline): void {
    const modalRef = this.modalService.open(AirlineDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.airline = airline;
  }
}
