import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { PassengerComponent } from './passenger.component';
import { PassengerDetailComponent } from './passenger-detail.component';
import { PassengerUpdateComponent } from './passenger-update.component';
import { PassengerDeleteDialogComponent } from './passenger-delete-dialog.component';
import { passengerRoute } from './passenger.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(passengerRoute)],
  declarations: [PassengerComponent, PassengerDetailComponent, PassengerUpdateComponent, PassengerDeleteDialogComponent],
  entryComponents: [PassengerDeleteDialogComponent],
})
export class PassengersPassengerModule {}
