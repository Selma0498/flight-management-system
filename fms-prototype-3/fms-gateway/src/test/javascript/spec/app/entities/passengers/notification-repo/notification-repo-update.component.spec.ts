import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { NotificationRepoUpdateComponent } from 'app/entities/passengers/notification-repo/notification-repo-update.component';
import { NotificationRepoService } from 'app/entities/passengers/notification-repo/notification-repo.service';
import { NotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

describe('Component Tests', () => {
  describe('NotificationRepo Management Update Component', () => {
    let comp: NotificationRepoUpdateComponent;
    let fixture: ComponentFixture<NotificationRepoUpdateComponent>;
    let service: NotificationRepoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NotificationRepoUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(NotificationRepoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationRepoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NotificationRepoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NotificationRepo(123);
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
        const entity = new NotificationRepo();
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
