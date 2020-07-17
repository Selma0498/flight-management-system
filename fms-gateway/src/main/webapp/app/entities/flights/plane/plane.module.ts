import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { PlaneComponent } from './plane.component';
import { PlaneDetailComponent } from './plane-detail.component';
import { PlaneUpdateComponent } from './plane-update.component';
import { PlaneDeleteDialogComponent } from './plane-delete-dialog.component';
import { planeRoute } from './plane.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(planeRoute)],
  declarations: [PlaneComponent, PlaneDetailComponent, PlaneUpdateComponent, PlaneDeleteDialogComponent],
  entryComponents: [PlaneDeleteDialogComponent],
})
export class FlightsPlaneModule {}
