import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FlightUpdateComponent } from 'app/entities/flights/flight/flight-update.component';
import { FlightService } from 'app/entities/flights/flight/flight.service';
import { Flight } from 'app/shared/model/flights/flight.model';

describe('Component Tests', () => {
  describe('Flight Management Update Component', () => {
    let comp: FlightUpdateComponent;
    let fixture: ComponentFixture<FlightUpdateComponent>;
    let service: FlightService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FlightUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FlightUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FlightUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FlightService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Flight(123);
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
        const entity = new Flight();
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
