import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { FlightHandlingComponent } from './flight-handling.component';
import { FlightHandlingDetailComponent } from './flight-handling-detail.component';
import { FlightHandlingUpdateComponent } from './flight-handling-update.component';
import { FlightHandlingDeleteDialogComponent } from './flight-handling-delete-dialog.component';
import { flightHandlingRoute } from './flight-handling.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(flightHandlingRoute)],
  declarations: [
    FlightHandlingComponent,
    FlightHandlingDetailComponent,
    FlightHandlingUpdateComponent,
    FlightHandlingDeleteDialogComponent,
  ],
  entryComponents: [FlightHandlingDeleteDialogComponent],
})
export class FlightsFlightHandlingModule {}
