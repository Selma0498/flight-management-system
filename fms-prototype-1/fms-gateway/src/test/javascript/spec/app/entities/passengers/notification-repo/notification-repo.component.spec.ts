import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { NotificationRepoComponent } from 'app/entities/passengers/notification-repo/notification-repo.component';
import { NotificationRepoService } from 'app/entities/passengers/notification-repo/notification-repo.service';
import { NotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

describe('Component Tests', () => {
  describe('NotificationRepo Management Component', () => {
    let comp: NotificationRepoComponent;
    let fixture: ComponentFixture<NotificationRepoComponent>;
    let service: NotificationRepoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NotificationRepoComponent],
      })
        .overrideTemplate(NotificationRepoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationRepoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NotificationRepoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new NotificationRepo(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.notificationRepos && comp.notificationRepos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
