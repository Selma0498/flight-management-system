import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { INotification } from 'app/shared/model/notifications/notification.model';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";

@Injectable({ providedIn: 'root' })
export class NotificationService {
  public notifyUrl = SERVER_API_URL + 'services/notifications/api/notify?notificationType=';

  constructor(protected http: HttpClient) {}

  public getNotification(notificationType: ENotificationType): Observable<INotification> {
    return this.http.get<INotification>(
      `${this.notifyUrl}${notificationType}`);
  }
}
