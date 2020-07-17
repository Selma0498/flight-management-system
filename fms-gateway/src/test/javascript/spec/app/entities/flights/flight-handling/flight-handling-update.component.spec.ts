import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FlightHandlingUpdateComponent } from 'app/entities/flights/flight-handling/flight-handling-update.component';
import { FlightHandlingService } from 'app/entities/flights/flight-handling/flight-handling.service';
import { FlightHandling } from 'app/shared/model/flights/flight-handling.model';

describe('Component Tests', () => {
  describe('FlightHandling Management Update Component', () => {
    let comp: FlightHandlingUpdateComponent;
    let fixture: ComponentFixture<FlightHandlingUpdateComponent>;
    let service: FlightHandlingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FlightHandlingUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FlightHandlingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FlightHandlingUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FlightHandlingService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FlightHandling(123);
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
        const entity = new FlightHandling();
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
