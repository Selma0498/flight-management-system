import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { AirlineDetailComponent } from 'app/entities/flights/airline/airline-detail.component';
import { Airline } from 'app/shared/model/flights/airline.model';

describe('Component Tests', () => {
  describe('Airline Management Detail Component', () => {
    let comp: AirlineDetailComponent;
    let fixture: ComponentFixture<AirlineDetailComponent>;
    const route = ({ data: of({ airline: new Airline(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [AirlineDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AirlineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AirlineDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load airline on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.airline).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
