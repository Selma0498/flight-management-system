import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAirline, Airline } from 'app/shared/model/flights/airline.model';
import { AirlineService } from './airline.service';
import { AirlineComponent } from './airline.component';
import { AirlineDetailComponent } from './airline-detail.component';
import { AirlineUpdateComponent } from './airline-update.component';

@Injectable({ providedIn: 'root' })
export class AirlineResolve implements Resolve<IAirline> {
  constructor(private service: AirlineService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAirline> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((airline: HttpResponse<Airline>) => {
          if (airline.body) {
            return of(airline.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Airline());
  }
}

export const airlineRoute: Routes = [
  {
    path: '',
    component: AirlineComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airlines',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AirlineDetailComponent,
    resolve: {
      airline: AirlineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airlines',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AirlineUpdateComponent,
    resolve: {
      airline: AirlineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airlines',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AirlineUpdateComponent,
    resolve: {
      airline: AirlineResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airlines',
    },
    canActivate: [UserRouteAccessService],
  },
];
