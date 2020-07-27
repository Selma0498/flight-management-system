import { IPayment } from 'app/shared/model/payments/payment.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: number;
  amount?: number;
  passengerId?: number;
  bookingNumber?: number;
  payment?: IPayment;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: number,
    public amount?: number,
    public passengerId?: number,
    public bookingNumber?: number,
    public payment?: IPayment
  ) {}
}
