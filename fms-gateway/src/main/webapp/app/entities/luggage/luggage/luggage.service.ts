import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILuggage } from 'app/shared/model/luggage/luggage.model';

type EntityResponseType = HttpResponse<ILuggage>;
type EntityArrayResponseType = HttpResponse<ILuggage[]>;

@Injectable({ providedIn: 'root' })
export class LuggageService {
  public resourceUrl = SERVER_API_URL + 'services/luggage/api/luggages';

  constructor(protected http: HttpClient) {}

  create(luggage: ILuggage): Observable<EntityResponseType> {
    return this.http.post<ILuggage>(this.resourceUrl, luggage, { observe: 'response' });
  }

  update(luggage: ILuggage): Observable<EntityResponseType> {
    return this.http.put<ILuggage>(this.resourceUrl, luggage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILuggage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILuggage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
