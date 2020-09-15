import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { NotificationRepoComponent } from './notification-repo.component';
import { NotificationRepoDetailComponent } from './notification-repo-detail.component';
import { notificationRepoRoute } from './notification-repo.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(notificationRepoRoute)],
  declarations: [
    NotificationRepoComponent,
    NotificationRepoDetailComponent,
  ],
})
export class PassengersNotificationRepoModule {}
