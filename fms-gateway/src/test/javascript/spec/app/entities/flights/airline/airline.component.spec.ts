import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { AirlineComponent } from 'app/entities/flights/airline/airline.component';
import { AirlineService } from 'app/entities/flights/airline/airline.service';
import { Airline } from 'app/shared/model/flights/airline.model';

describe('Component Tests', () => {
  describe('Airline Management Component', () => {
    let comp: AirlineComponent;
    let fixture: ComponentFixture<AirlineComponent>;
    let service: AirlineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [AirlineComponent],
      })
        .overrideTemplate(AirlineComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AirlineComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AirlineService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Airline(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.airlines && comp.airlines[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
