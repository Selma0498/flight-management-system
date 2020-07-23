import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'flight',
        loadChildren: () => import('./flights/flight/flight.module').then(m => m.FlightsFlightModule),
      },
      {
        path: 'flight-handling',
        loadChildren: () => import('./flights/flight-handling/flight-handling.module').then(m => m.FlightsFlightHandlingModule),
      },
      {
        path: 'airport',
        loadChildren: () => import('./flights/airport/airport.module').then(m => m.FlightsAirportModule),
      },
      {
        path: 'airline',
        loadChildren: () => import('./flights/airline/airline.module').then(m => m.FlightsAirlineModule),
      },
      {
        path: 'plane',
        loadChildren: () => import('./flights/plane/plane.module').then(m => m.FlightsPlaneModule),
      },
      ,
      {
        path: 'booking',
        loadChildren: () => import('./bookings/booking/booking.module').then(m => m.BookingsBookingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GatewayEntityModule {}
