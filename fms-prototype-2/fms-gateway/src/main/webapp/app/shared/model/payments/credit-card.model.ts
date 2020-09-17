import { Moment } from 'moment';
import { IPayment } from 'app/shared/model/payments/payment.model';
import { ECardType } from 'app/shared/model/enumerations/e-card-type.model';

export interface ICreditCard {
  id?: number;
  cardType?: ECardType;
  cvc?: number;
  cardNumber?: number;
  validityDate?: Moment;
  payment?: IPayment;
}

export class CreditCard implements ICreditCard {
  constructor(
    public id?: number,
    public cardType?: ECardType,
    public cvc?: number,
    public cardNumber?: number,
    public validityDate?: Moment,
    public payment?: IPayment
  ) {}
}
