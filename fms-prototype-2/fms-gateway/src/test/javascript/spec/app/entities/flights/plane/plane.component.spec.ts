import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { PlaneComponent } from 'app/entities/flights/plane/plane.component';
import { PlaneService } from 'app/entities/flights/plane/plane.service';
import { Plane } from 'app/shared/model/flights/plane.model';

describe('Component Tests', () => {
  describe('Plane Management Component', () => {
    let comp: PlaneComponent;
    let fixture: ComponentFixture<PlaneComponent>;
    let service: PlaneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PlaneComponent],
      })
        .overrideTemplate(PlaneComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlaneComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlaneService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Plane(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.planes && comp.planes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
