import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFlight } from 'app/shared/model/flights/flight.model';
import { FlightService } from './flight.service';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";
import {NotificationService} from "app/entities/notifications/notification/notification.service";
import {NotificationRepo} from "app/shared/model/passengers/notification-repo.model";
import {NotificationRepoService} from "app/entities/passengers/notification-repo/notification-repo.service";

@Component({
  templateUrl: './flight-delete-dialog.component.html',
})
export class FlightDeleteDialogComponent {
  flight?: IFlight;

  constructor(
    protected flightService: FlightService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private notificationService: NotificationService,
    private notificationRepo: NotificationRepo,
    private notificationRepoService: NotificationRepoService
    ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flightService.delete(id).subscribe(() => {
      this.eventManager.broadcast('flightListModification');
      this.activeModal.close();
    });

    //save notification in notification repo for passengers to see
    this.notificationRepo.name = "FLIGHT CANCELLED";
    this.notificationRepo.description = "Flight with id= " + id + " has been cancelled.";
    this.notificationRepoService.updateNotificationRepo(this.notificationRepo);

    this.notificationService.getNotification(ENotificationType.FLIGHT_CANCELLED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          window.confirm(notification.description);
        } else {
          window.confirm("undefined");
        }
      });
  }
}
