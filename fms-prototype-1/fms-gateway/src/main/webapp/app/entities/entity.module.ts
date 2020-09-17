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
        path: 'airport',
        loadChildren: () => import('./flights/airport/airport.module').then(m => m.FlightsAirportModule),
      },
      {
        path: 'booking',
        loadChildren: () => import('./bookings/booking/booking.module').then(m => m.BookingsBookingModule),
      },
      {
        path: 'luggage',
        loadChildren: () => import('./luggage/luggage/luggage.module').then(m => m.LuggageLuggageModule),
      },
      {
        path: 'notification',
        loadChildren: () => import('./notifications/notification/notification.module').then(m => m.NotificationsNotificationModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payments/payment/payment.module').then(m => m.PaymentsPaymentModule),
      },
      {
        path: 'credit-card',
        loadChildren: () => import('./payments/credit-card/credit-card.module').then(m => m.PaymentsCreditCardModule),
      },
      {
        path: 'passenger',
        loadChildren: () => import('./passengers/passenger/passenger.module').then(m => m.PassengersPassengerModule),
      },
      {
        path: 'notification-repo',
        loadChildren: () => import('./passengers/notification-repo/notification-repo.module').then(m => m.PassengersNotificationRepoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GatewayEntityModule {}
