import { IInvoice } from 'app/shared/model/payment/invoice.model';

export interface IPayment {
  id?: number;
  passengerId?: string;
  toPay?: number;
  bookingNumber?: number;
  invoice?: IInvoice;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public passengerId?: string,
    public toPay?: number,
    public bookingNumber?: number,
    public invoice?: IInvoice
  ) {}
}
