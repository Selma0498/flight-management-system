import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { PlaneUpdateComponent } from 'app/entities/flights/plane/plane-update.component';
import { PlaneService } from 'app/entities/flights/plane/plane.service';
import { Plane } from 'app/shared/model/flights/plane.model';

describe('Component Tests', () => {
  describe('Plane Management Update Component', () => {
    let comp: PlaneUpdateComponent;
    let fixture: ComponentFixture<PlaneUpdateComponent>;
    let service: PlaneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [PlaneUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PlaneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlaneUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlaneService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plane(123);
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
        const entity = new Plane();
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
