import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { NotificationComponent } from "app/entities/notifications/notification/notification.component";

@Injectable({ providedIn: 'root' })
export class NotificationResolve {

}
export const notificationRoute: Routes = [
  {
    path: 'notify/:notificationType',
    component: NotificationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Notifications',
    },
    canActivate: [UserRouteAccessService],
  },
];
