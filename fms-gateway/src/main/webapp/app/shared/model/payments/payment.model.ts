export interface IPayment {
  id?: number;
  passengerId?: string;
  toPay?: number;
}

export class Payment implements IPayment {
  constructor(public id?: number, public passengerId?: string, public toPay?: number) {}
}
