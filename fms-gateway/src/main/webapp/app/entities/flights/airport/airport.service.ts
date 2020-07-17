import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAirport } from 'app/shared/model/flights/airport.model';

type EntityResponseType = HttpResponse<IAirport>;
type EntityArrayResponseType = HttpResponse<IAirport[]>;

@Injectable({ providedIn: 'root' })
export class AirportService {
  public resourceUrl = SERVER_API_URL + 'services/flights/api/airports';

  constructor(protected http: HttpClient) {}

  create(airport: IAirport): Observable<EntityResponseType> {
    return this.http.post<IAirport>(this.resourceUrl, airport, { observe: 'response' });
  }

  update(airport: IAirport): Observable<EntityResponseType> {
    return this.http.put<IAirport>(this.resourceUrl, airport, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAirport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAirport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
