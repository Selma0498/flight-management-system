import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FlightHandlingDetailComponent } from 'app/entities/flights/flight-handling/flight-handling-detail.component';
import { FlightHandling } from 'app/shared/model/flights/flight-handling.model';

describe('Component Tests', () => {
  describe('FlightHandling Management Detail Component', () => {
    let comp: FlightHandlingDetailComponent;
    let fixture: ComponentFixture<FlightHandlingDetailComponent>;
    const route = ({ data: of({ flightHandling: new FlightHandling(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FlightHandlingDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FlightHandlingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FlightHandlingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load flightHandling on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.flightHandling).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
