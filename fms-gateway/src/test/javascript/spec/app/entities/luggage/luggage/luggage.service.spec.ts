import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LuggageService } from 'app/entities/luggage/luggage/luggage.service';
import { ILuggage, Luggage } from 'app/shared/model/luggage/luggage.model';
import { ELuggageType } from 'app/shared/model/enumerations/e-luggage-type.model';

describe('Service Tests', () => {
  describe('Luggage Service', () => {
    let injector: TestBed;
    let service: LuggageService;
    let httpMock: HttpTestingController;
    let elemDefault: ILuggage;
    let expectedResult: ILuggage | ILuggage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LuggageService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Luggage(0, ELuggageType.CARRY_ON, 0, 'AAAAAAA', 0, 'AAAAAAA', 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Luggage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Luggage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Luggage', () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            luggageNumber: 1,
            flightNumber: 'BBBBBB',
            bookingNumber: 1,
            passengerId: 'BBBBBB',
            weightCategory: 1,
            rfidTag: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Luggage', () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            luggageNumber: 1,
            flightNumber: 'BBBBBB',
            bookingNumber: 1,
            passengerId: 'BBBBBB',
            weightCategory: 1,
            rfidTag: 'BBBBBB',
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

      it('should delete a Luggage', () => {
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
