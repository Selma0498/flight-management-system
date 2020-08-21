import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { PlaneDetailComponent } from 'app/entities/flights/plane/plane-detail.component';
import { Plane } from 'app/shared/model/flights/plane.model';

describe('Component Tests', () => {
  describe('Plane Management Detail Component', () => {
    let comp: PlaneDetailComponent;
    let fixture: ComponentFixture<PlaneDetailComponent>;
    const route = ({ data: of({ plane: new Plane(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PlaneDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PlaneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlaneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load plane on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plane).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
