import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { LuggageComponent } from 'app/entities/luggage/luggage/luggage.component';
import { LuggageService } from 'app/entities/luggage/luggage/luggage.service';
import { Luggage } from 'app/shared/model/luggage/luggage.model';

describe('Component Tests', () => {
  describe('Luggage Management Component', () => {
    let comp: LuggageComponent;
    let fixture: ComponentFixture<LuggageComponent>;
    let service: LuggageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [LuggageComponent],
      })
        .overrideTemplate(LuggageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LuggageComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LuggageService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Luggage(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.luggages && comp.luggages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
