import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { CreditCardUpdateComponent } from 'app/entities/payments/credit-card/credit-card-update.component';
import { CreditCardService } from 'app/entities/payments/credit-card/credit-card.service';
import { CreditCard } from 'app/shared/model/payments/credit-card.model';

describe('Component Tests', () => {
  describe('CreditCard Management Update Component', () => {
    let comp: CreditCardUpdateComponent;
    let fixture: ComponentFixture<CreditCardUpdateComponent>;
    let service: CreditCardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [CreditCardUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CreditCardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CreditCardUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CreditCardService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CreditCard(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CreditCard();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
