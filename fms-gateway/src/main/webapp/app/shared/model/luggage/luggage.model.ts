import { ELuggageType } from 'app/shared/model/enumerations/e-luggage-type.model';

export interface ILuggage {
  id?: number;
  role?: ELuggageType;
  luggageNumber?: number;
  flightNumber?: string;
  passengerId?: string;
  weight?: number;
  rfidTag?: string;
}

export class Luggage implements ILuggage {
  constructor(
    public id?: number,
    public role?: ELuggageType,
    public luggageNumber?: number,
    public flightNumber?: string,
    public passengerId?: string,
    public weight?: number,
    public rfidTag?: string
  ) {}
}
