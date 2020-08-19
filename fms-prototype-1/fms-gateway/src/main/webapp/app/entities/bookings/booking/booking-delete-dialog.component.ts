import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBooking } from 'app/shared/model/bookings/booking.model';
import { BookingService } from './booking.service';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";
import {NotificationService} from "app/entities/notifications/notification/notification.service";

@Component({
  templateUrl: './booking-delete-dialog.component.html',
})
export class BookingDeleteDialogComponent {
  booking?: IBooking;

  constructor(
    protected bookingService: BookingService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private notificationService: NotificationService
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bookingListModification');
      this.activeModal.close();
    });
    this.notificationService.getNotification(ENotificationType.BOOKING_CANCELLED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          window.confirm(notification.description);
        } else {
          window.confirm("undefined");
        }
      });
  }
}
