import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import {INotificationRepo, NotificationRepo} from 'app/shared/model/passengers/notification-repo.model';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";

type EntityResponseType = HttpResponse<INotificationRepo>;
type EntityArrayResponseType = HttpResponse<INotificationRepo[]>;

@Injectable({ providedIn: 'root' })
export class NotificationRepoService {
  public resourceUrl = SERVER_API_URL + 'services/passengers/api/notification-repos';
  public saveNotificationUrl = SERVER_API_URL + 'services/passengers/api/notification-repos/notification-repos';


  constructor(protected http: HttpClient) {}

  create(notificationRepo: INotificationRepo): Observable<EntityResponseType> {
    return this.http.post<INotificationRepo>(this.resourceUrl, notificationRepo, { observe: 'response' });
  }

  update(notificationRepo: INotificationRepo): Observable<EntityResponseType> {
    return this.http.put<INotificationRepo>(this.resourceUrl, notificationRepo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotificationRepo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotificationRepo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  public updateNotificationRepo(notificationType: ENotificationType, id: number): Observable<EntityResponseType> {
    return this.http.post(this.saveNotificationUrl,
      new NotificationRepo(undefined, "FLIGHT CANCELLED", "Flight with id= " + id + " has been cancelled."),
      { observe: 'response' });
  }
}
