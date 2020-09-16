import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAirport, Airport } from 'app/shared/model/flights/airport.model';
import { AirportService } from './airport.service';
import { AirportComponent } from './airport.component';
import { AirportDetailComponent } from './airport-detail.component';
import { AirportUpdateComponent } from './airport-update.component';

@Injectable({ providedIn: 'root' })
export class AirportResolve implements Resolve<IAirport> {
  constructor(private service: AirportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAirport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((airport: HttpResponse<Airport>) => {
          if (airport.body) {
            return of(airport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Airport());
  }
}

export const airportRoute: Routes = [
  {
    path: '',
    component: AirportComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AirportDetailComponent,
    resolve: {
      airport: AirportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AirportUpdateComponent,
    resolve: {
      airport: AirportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AirportUpdateComponent,
    resolve: {
      airport: AirportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Airports',
    },
    canActivate: [UserRouteAccessService],
  },
];
