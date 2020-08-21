import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { IAirport } from 'app/shared/model/flights/airport.model';
import { IAirline } from 'app/shared/model/flights/airline.model';
import { IPlane } from 'app/shared/model/flights/plane.model';
import { EFlightType } from 'app/shared/model/enumerations/e-flight-type.model';
import { EFareType } from 'app/shared/model/enumerations/e-fare-type.model';

export interface IFlight {
  id?: number;
  flightNumber?: string;
  flightType?: EFlightType;
  fareType?: EFareType;
  pilot?: string;
  price?: number;
  flightHandler?: IFlightHandling;
  origin?: IAirport;
  destination?: IAirport;
  airline?: IAirline;
  plane?: IPlane;
}

export class Flight implements IFlight {
  constructor(
    public id?: number,
    public flightNumber?: string,
    public flightType?: EFlightType,
    public fareType?: EFareType,
    public pilot?: string,
    public price?: number,
    public flightHandler?: IFlightHandling,
    public origin?: IAirport,
    public destination?: IAirport,
    public airline?: IAirline,
    public plane?: IPlane
  ) {}
}
