import { ICreditCard } from 'app/shared/model/payments/credit-card.model';

export interface IPayment {
  id?: number;
  passengerId?: string;
  toPay?: number;
  bookingNumber?: number;
  creditCard?: ICreditCard;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public passengerId?: string,
    public toPay?: number,
    public bookingNumber?: number,
    public creditCard?: ICreditCard
  ) {}
}
