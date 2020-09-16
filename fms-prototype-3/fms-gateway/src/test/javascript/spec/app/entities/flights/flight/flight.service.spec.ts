import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { FlightService } from 'app/entities/flights/flight/flight.service';
import { IFlight, Flight } from 'app/shared/model/flights/flight.model';
import { EFlightType } from 'app/shared/model/enumerations/e-flight-type.model';
import { EFareType } from 'app/shared/model/enumerations/e-fare-type.model';

describe('Service Tests', () => {
  describe('Flight Service', () => {
    let injector: TestBed;
    let service: FlightService;
    let httpMock: HttpTestingController;
    let elemDefault: IFlight;
    let expectedResult: IFlight | IFlight[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FlightService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Flight(0, 'AAAAAAA', EFlightType.ONE_WAY, EFareType.ECONOMY, 'AAAAAAA', 'AAAAAAA', 0, currentDate, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            departureDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Flight', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            departureDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Flight()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Flight', () => {
        const returnedFromService = Object.assign(
          {
            flightNumber: 'BBBBBB',
            flightType: 'BBBBBB',
            fareType: 'BBBBBB',
            pilot: 'BBBBBB',
            planeModelNumber: 'BBBBBB',
            price: 1,
            departureDate: currentDate.format(DATE_FORMAT),
            boardingGate: 1,
            airlineName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Flight', () => {
        const returnedFromService = Object.assign(
          {
            flightNumber: 'BBBBBB',
            flightType: 'BBBBBB',
            fareType: 'BBBBBB',
            pilot: 'BBBBBB',
            planeModelNumber: 'BBBBBB',
            price: 1,
            departureDate: currentDate.format(DATE_FORMAT),
            boardingGate: 1,
            airlineName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Flight', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
