import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { NotificationRepoDetailComponent } from 'app/entities/passengers/notification-repo/notification-repo-detail.component';
import { NotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

describe('Component Tests', () => {
  describe('NotificationRepo Management Detail Component', () => {
    let comp: NotificationRepoDetailComponent;
    let fixture: ComponentFixture<NotificationRepoDetailComponent>;
    const route = ({ data: of({ notificationRepo: new NotificationRepo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [NotificationRepoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(NotificationRepoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificationRepoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notificationRepo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notificationRepo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
