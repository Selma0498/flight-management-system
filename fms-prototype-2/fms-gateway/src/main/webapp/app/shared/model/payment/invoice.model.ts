import { IPayment } from 'app/shared/model/payment/payment.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: number;
  amount?: number;
  passengerId?: string;
  bookingNumber?: number;
  payment?: IPayment;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: number,
    public amount?: number,
    public passengerId?: string,
    public bookingNumber?: number,
    public payment?: IPayment
  ) {}
}
