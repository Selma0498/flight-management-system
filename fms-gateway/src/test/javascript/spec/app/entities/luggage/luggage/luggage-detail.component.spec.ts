import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { LuggageDetailComponent } from 'app/entities/luggage/luggage/luggage-detail.component';
import { Luggage } from 'app/shared/model/luggage/luggage.model';

describe('Component Tests', () => {
  describe('Luggage Management Detail Component', () => {
    let comp: LuggageDetailComponent;
    let fixture: ComponentFixture<LuggageDetailComponent>;
    const route = ({ data: of({ luggage: new Luggage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [LuggageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LuggageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LuggageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load luggage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.luggage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
