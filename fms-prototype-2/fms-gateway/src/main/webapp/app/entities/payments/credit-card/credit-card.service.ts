import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICreditCard } from 'app/shared/model/payments/credit-card.model';

type EntityResponseType = HttpResponse<ICreditCard>;
type EntityArrayResponseType = HttpResponse<ICreditCard[]>;

@Injectable({ providedIn: 'root' })
export class CreditCardService {
  public resourceUrl = SERVER_API_URL + 'services/payments/api/credit-cards';

  constructor(protected http: HttpClient) {}

  create(creditCard: ICreditCard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditCard);
    return this.http
      .post<ICreditCard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(creditCard: ICreditCard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditCard);
    return this.http
      .put<ICreditCard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICreditCard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICreditCard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(creditCard: ICreditCard): ICreditCard {
    const copy: ICreditCard = Object.assign({}, creditCard, {
      validityDate: creditCard.validityDate && creditCard.validityDate.isValid() ? creditCard.validityDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validityDate = res.body.validityDate ? moment(res.body.validityDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((creditCard: ICreditCard) => {
        creditCard.validityDate = creditCard.validityDate ? moment(creditCard.validityDate) : undefined;
      });
    }
    return res;
  }
}
