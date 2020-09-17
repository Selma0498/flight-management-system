import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CreditCardDetailComponent } from 'app/entities/payments/credit-card/credit-card-detail.component';
import { CreditCard } from 'app/shared/model/payments/credit-card.model';

describe('Component Tests', () => {
  describe('CreditCard Management Detail Component', () => {
    let comp: CreditCardDetailComponent;
    let fixture: ComponentFixture<CreditCardDetailComponent>;
    const route = ({ data: of({ creditCard: new CreditCard(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CreditCardDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CreditCardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CreditCardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load creditCard on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.creditCard).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
