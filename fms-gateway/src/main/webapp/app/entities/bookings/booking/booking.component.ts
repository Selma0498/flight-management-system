import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooking } from 'app/shared/model/bookings/booking.model';
import { BookingService } from './booking.service';
import { BookingDeleteDialogComponent } from './booking-delete-dialog.component';

@Component({
  selector: 'jhi-booking',
  templateUrl: './booking.component.html',
})
export class BookingComponent implements OnInit, OnDestroy {
  bookings?: IBooking[];
  eventSubscriber?: Subscription;

  constructor(protected bookingService: BookingService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.bookingService.query().subscribe((res: HttpResponse<IBooking[]>) => (this.bookings = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBookings();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBooking): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBookings(): void {
    this.eventSubscriber = this.eventManager.subscribe('bookingListModification', () => this.loadAll());
  }

  delete(booking: IBooking): void {
    const modalRef = this.modalService.open(BookingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.booking = booking;
  }
}
