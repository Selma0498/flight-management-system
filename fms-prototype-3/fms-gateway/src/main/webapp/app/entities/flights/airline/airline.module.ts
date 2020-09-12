import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { AirlineComponent } from './airline.component';
import { AirlineDetailComponent } from './airline-detail.component';
import { AirlineUpdateComponent } from './airline-update.component';
import { AirlineDeleteDialogComponent } from './airline-delete-dialog.component';
import { airlineRoute } from './airline.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(airlineRoute)],
  declarations: [AirlineComponent, AirlineDetailComponent, AirlineUpdateComponent, AirlineDeleteDialogComponent],
  entryComponents: [AirlineDeleteDialogComponent],
})
export class FlightsAirlineModule {}
