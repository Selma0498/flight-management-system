import { Moment } from 'moment';
import { IAirport } from 'app/shared/model/flights/airport.model';
import { EFlightType } from 'app/shared/model/enumerations/e-flight-type.model';
import { EFareType } from 'app/shared/model/enumerations/e-fare-type.model';

export interface IFlight {
  id?: number;
  flightNumber?: string;
  flightType?: EFlightType;
  fareType?: EFareType;
  pilot?: string;
  planeModelNumber?: string;
  price?: number;
  departureDate?: Moment;
  boardingGate?: number;
  airlineName?: string;
  origin?: IAirport;
  destination?: IAirport;
}

export class Flight implements IFlight {
  constructor(
    public id?: number,
    public flightNumber?: string,
    public flightType?: EFlightType,
    public fareType?: EFareType,
    public pilot?: string,
    public planeModelNumber?: string,
    public price?: number,
    public departureDate?: Moment,
    public boardingGate?: number,
    public airlineName?: string,
    public origin?: IAirport,
    public destination?: IAirport
  ) {}
}
