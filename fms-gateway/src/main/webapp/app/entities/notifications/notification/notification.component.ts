import {Component, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {NotificationService} from './notification.service';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";

@Component({
  selector: 'jhi-notification',
  templateUrl: './notification.component.html'
})
export class NotificationComponent implements OnInit {

  public notificationMessage = "";

  constructor(protected notificationService: NotificationService) {}

  ngOnInit(): void {
    /*this.notificationService.getNotification(ENotificationType.BOOKING_CONFIRMED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          this.notificationMessage = notification.description;
          window.confirm(this.notificationMessage);
          window.confirm("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        } else {
          this.notificationMessage = "unknown";
          window.confirm(this.notificationMessage);
          window.confirm("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAA ELSEEEEEEEEEE");
        }
      });*/
  }
}
