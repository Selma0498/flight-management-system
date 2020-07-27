import { IInvoice } from 'app/shared/model/payments/invoice.model';

export interface IPayment {
  id?: number;
  passengerId?: number;
  toPay?: number;
  invoice?: IInvoice;
}

export class Payment implements IPayment {
  constructor(public id?: number, public passengerId?: number, public toPay?: number, public invoice?: IInvoice) {}
}
