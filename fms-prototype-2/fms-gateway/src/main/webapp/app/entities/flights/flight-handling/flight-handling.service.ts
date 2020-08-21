import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';

type EntityResponseType = HttpResponse<IFlightHandling>;
type EntityArrayResponseType = HttpResponse<IFlightHandling[]>;

@Injectable({ providedIn: 'root' })
export class FlightHandlingService {
  public resourceUrl = SERVER_API_URL + 'services/flights/api/flight-handlings';

  constructor(protected http: HttpClient) {}

  create(flightHandling: IFlightHandling): Observable<EntityResponseType> {
    return this.http.post<IFlightHandling>(this.resourceUrl, flightHandling, { observe: 'response' });
  }

  update(flightHandling: IFlightHandling): Observable<EntityResponseType> {
    return this.http.put<IFlightHandling>(this.resourceUrl, flightHandling, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFlightHandling>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFlightHandling[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
