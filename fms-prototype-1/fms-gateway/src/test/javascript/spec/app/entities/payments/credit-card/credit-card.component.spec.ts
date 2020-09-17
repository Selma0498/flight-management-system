import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { CreditCardComponent } from 'app/entities/payments/credit-card/credit-card.component';
import { CreditCardService } from 'app/entities/payments/credit-card/credit-card.service';
import { CreditCard } from 'app/shared/model/payments/credit-card.model';

describe('Component Tests', () => {
  describe('CreditCard Management Component', () => {
    let comp: CreditCardComponent;
    let fixture: ComponentFixture<CreditCardComponent>;
    let service: CreditCardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CreditCardComponent],
      })
        .overrideTemplate(CreditCardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CreditCardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CreditCardService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CreditCard(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.creditCards && comp.creditCards[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
