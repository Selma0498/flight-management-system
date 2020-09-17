import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CreditCardService } from 'app/entities/payments/credit-card/credit-card.service';
import { ICreditCard, CreditCard } from 'app/shared/model/payments/credit-card.model';
import { ECardType } from 'app/shared/model/enumerations/e-card-type.model';

describe('Service Tests', () => {
  describe('CreditCard Service', () => {
    let injector: TestBed;
    let service: CreditCardService;
    let httpMock: HttpTestingController;
    let elemDefault: ICreditCard;
    let expectedResult: ICreditCard | ICreditCard[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CreditCardService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CreditCard(0, ECardType.MASTERCARD, 0, 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            validityDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CreditCard', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            validityDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validityDate: currentDate,
          },
          returnedFromService
        );

        service.create(new CreditCard()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CreditCard', () => {
        const returnedFromService = Object.assign(
          {
            cardType: 'BBBBBB',
            cvc: 1,
            cardNumber: 1,
            validityDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validityDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CreditCard', () => {
        const returnedFromService = Object.assign(
          {
            cardType: 'BBBBBB',
            cvc: 1,
            cardNumber: 1,
            validityDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validityDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CreditCard', () => {
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
