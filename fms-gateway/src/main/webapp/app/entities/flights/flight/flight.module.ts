import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { FlightComponent } from './flight.component';
import { FlightDetailComponent } from './flight-detail.component';
import { FlightUpdateComponent } from './flight-update.component';
import { FlightDeleteDialogComponent } from './flight-delete-dialog.component';
import { flightRoute } from './flight.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(flightRoute)],
  declarations: [FlightComponent, FlightDetailComponent, FlightUpdateComponent, FlightDeleteDialogComponent],
  entryComponents: [FlightDeleteDialogComponent],
})
export class FlightsFlightModule {}
