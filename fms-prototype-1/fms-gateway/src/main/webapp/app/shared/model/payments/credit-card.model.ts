import { Moment } from 'moment';
import { ECardType } from 'app/shared/model/enumerations/e-card-type.model';

export interface ICreditCard {
  id?: number;
  cardType?: ECardType;
  cvc?: number;
  cardNumber?: number;
  validityDate?: Moment;
}

export class CreditCard implements ICreditCard {
  constructor(
    public id?: number,
    public cardType?: ECardType,
    public cvc?: number,
    public cardNumber?: number,
    public validityDate?: Moment
  ) {}
}
