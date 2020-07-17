import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
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

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FlightService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Flight(0, 'AAAAAAA', EFlightType.ONE_WAY, EFareType.ECONOMY, 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Flight', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

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
            price: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

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
            price: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

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
