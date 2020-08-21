import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { LuggageComponent } from './luggage.component';
import { LuggageDetailComponent } from './luggage-detail.component';
import { LuggageUpdateComponent } from './luggage-update.component';
import { LuggageDeleteDialogComponent } from './luggage-delete-dialog.component';
import { luggageRoute } from './luggage.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(luggageRoute)],
  declarations: [LuggageComponent, LuggageDetailComponent, LuggageUpdateComponent, LuggageDeleteDialogComponent],
  entryComponents: [LuggageDeleteDialogComponent],
})
export class LuggageLuggageModule {}
