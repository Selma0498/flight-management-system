export interface IAirport {
  id?: number;
  airportCode?: string;
  airportName?: string;
  countryName?: string;
  cityName?: string;
  postalCode?: string;
}

export class Airport implements IAirport {
  constructor(
    public id?: number,
    public airportCode?: string,
    public airportName?: string,
    public countryName?: string,
    public cityName?: string,
    public postalCode?: string
  ) {}
}
