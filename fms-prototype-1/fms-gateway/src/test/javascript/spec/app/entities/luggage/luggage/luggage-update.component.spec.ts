import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { LuggageUpdateComponent } from 'app/entities/luggage/luggage/luggage-update.component';
import { LuggageService } from 'app/entities/luggage/luggage/luggage.service';
import { Luggage } from 'app/shared/model/luggage/luggage.model';

describe('Component Tests', () => {
  describe('Luggage Management Update Component', () => {
    let comp: LuggageUpdateComponent;
    let fixture: ComponentFixture<LuggageUpdateComponent>;
    let service: LuggageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [LuggageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LuggageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LuggageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LuggageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Luggage(123);
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
        const entity = new Luggage();
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
