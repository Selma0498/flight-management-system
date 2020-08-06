
export interface IPayment {
  id?: number;
  passengerId?: number;
  toPay?: number;
}

export class Payment implements IPayment {
  constructor(public id?: number, public passengerId?: number, public toPay?: number) {}
}
