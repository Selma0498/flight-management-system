import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { FlightHandlingComponent } from 'app/entities/flights/flight-handling/flight-handling.component';
import { FlightHandlingService } from 'app/entities/flights/flight-handling/flight-handling.service';
import { FlightHandling } from 'app/shared/model/flights/flight-handling.model';

describe('Component Tests', () => {
  describe('FlightHandling Management Component', () => {
    let comp: FlightHandlingComponent;
    let fixture: ComponentFixture<FlightHandlingComponent>;
    let service: FlightHandlingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FlightHandlingComponent],
      })
        .overrideTemplate(FlightHandlingComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FlightHandlingComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FlightHandlingService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FlightHandling(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.flightHandlings && comp.flightHandlings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
