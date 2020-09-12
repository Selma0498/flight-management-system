export interface IAirline {
  id?: number;
  airlineName?: string;
}

export class Airline implements IAirline {
  constructor(public id?: number, public airlineName?: string) {}
}
