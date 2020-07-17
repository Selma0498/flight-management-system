import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFlightHandling, FlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { FlightHandlingService } from './flight-handling.service';
import { FlightHandlingComponent } from './flight-handling.component';
import { FlightHandlingDetailComponent } from './flight-handling-detail.component';
import { FlightHandlingUpdateComponent } from './flight-handling-update.component';

@Injectable({ providedIn: 'root' })
export class FlightHandlingResolve implements Resolve<IFlightHandling> {
  constructor(private service: FlightHandlingService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlightHandling> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((flightHandling: HttpResponse<FlightHandling>) => {
          if (flightHandling.body) {
            return of(flightHandling.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FlightHandling());
  }
}

export const flightHandlingRoute: Routes = [
  {
    path: '',
    component: FlightHandlingComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FlightHandlings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FlightHandlingDetailComponent,
    resolve: {
      flightHandling: FlightHandlingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FlightHandlings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FlightHandlingUpdateComponent,
    resolve: {
      flightHandling: FlightHandlingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FlightHandlings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FlightHandlingUpdateComponent,
    resolve: {
      flightHandling: FlightHandlingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FlightHandlings',
    },
    canActivate: [UserRouteAccessService],
  },
];
