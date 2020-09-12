import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAirline } from 'app/shared/model/flights/airline.model';

type EntityResponseType = HttpResponse<IAirline>;
type EntityArrayResponseType = HttpResponse<IAirline[]>;

@Injectable({ providedIn: 'root' })
export class AirlineService {
  public resourceUrl = SERVER_API_URL + 'services/flights/api/airlines';

  constructor(protected http: HttpClient) {}

  create(airline: IAirline): Observable<EntityResponseType> {
    return this.http.post<IAirline>(this.resourceUrl, airline, { observe: 'response' });
  }

  update(airline: IAirline): Observable<EntityResponseType> {
    return this.http.put<IAirline>(this.resourceUrl, airline, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAirline>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAirline[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
