import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILuggage, Luggage } from 'app/shared/model/luggage/luggage.model';
import { LuggageService } from './luggage.service';
import { LuggageComponent } from './luggage.component';
import { LuggageDetailComponent } from './luggage-detail.component';
import { LuggageUpdateComponent } from './luggage-update.component';

@Injectable({ providedIn: 'root' })
export class LuggageResolve implements Resolve<ILuggage> {
  constructor(private service: LuggageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILuggage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((luggage: HttpResponse<Luggage>) => {
          if (luggage.body) {
            return of(luggage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Luggage());
  }
}

export const luggageRoute: Routes = [
  {
    path: '',
    component: LuggageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Luggages',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LuggageDetailComponent,
    resolve: {
      luggage: LuggageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Luggages',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new/flight/:flightNumber/booking/:bookingNumber/price/:flightPrice',
    component: LuggageUpdateComponent,
    resolve: {
      luggage: LuggageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Luggages',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LuggageUpdateComponent,
    resolve: {
      luggage: LuggageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Luggages',
    },
    canActivate: [UserRouteAccessService],
  },
];
