export interface IFlightHandling {
  id?: number;
  boardingGate?: number;
  delay?: number;
}

export class FlightHandling implements IFlightHandling {
  constructor(public id?: number, public boardingGate?: number, public delay?: number) {}
}
