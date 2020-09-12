import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlane, Plane } from 'app/shared/model/flights/plane.model';
import { PlaneService } from './plane.service';
import { PlaneComponent } from './plane.component';
import { PlaneDetailComponent } from './plane-detail.component';
import { PlaneUpdateComponent } from './plane-update.component';

@Injectable({ providedIn: 'root' })
export class PlaneResolve implements Resolve<IPlane> {
  constructor(private service: PlaneService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlane> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((plane: HttpResponse<Plane>) => {
          if (plane.body) {
            return of(plane.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plane());
  }
}

export const planeRoute: Routes = [
  {
    path: '',
    component: PlaneComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Planes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaneDetailComponent,
    resolve: {
      plane: PlaneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Planes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaneUpdateComponent,
    resolve: {
      plane: PlaneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Planes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaneUpdateComponent,
    resolve: {
      plane: PlaneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Planes',
    },
    canActivate: [UserRouteAccessService],
  },
];
