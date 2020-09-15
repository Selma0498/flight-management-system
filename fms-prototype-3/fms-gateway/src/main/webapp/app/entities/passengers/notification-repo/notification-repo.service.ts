import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { INotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

type EntityResponseType = HttpResponse<INotificationRepo>;
type EntityArrayResponseType = HttpResponse<INotificationRepo[]>;

@Injectable({ providedIn: 'root' })
export class NotificationRepoService {
  public resourceUrl = SERVER_API_URL + 'services/passengers/api/notification-repos';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotificationRepo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotificationRepo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

}
