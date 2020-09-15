import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INotificationRepo, NotificationRepo } from 'app/shared/model/passengers/notification-repo.model';
import { NotificationRepoService } from './notification-repo.service';
import { NotificationRepoComponent } from './notification-repo.component';
import { NotificationRepoDetailComponent } from './notification-repo-detail.component';

@Injectable({ providedIn: 'root' })
export class NotificationRepoResolve implements Resolve<INotificationRepo> {
  constructor(private service: NotificationRepoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotificationRepo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((notificationRepo: HttpResponse<NotificationRepo>) => {
          if (notificationRepo.body) {
            return of(notificationRepo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NotificationRepo());
  }
}

export const notificationRepoRoute: Routes = [
  {
    path: '',
    component: NotificationRepoComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NotificationRepos',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificationRepoDetailComponent,
    resolve: {
      notificationRepo: NotificationRepoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NotificationRepos',
    },
    canActivate: [UserRouteAccessService],
  },
];
