import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { NotificationComponent } from './notification.component';
import {RouterModule} from "@angular/router";
import {notificationRoute} from "app/entities/notifications/notification/notification.route";

@NgModule({
  imports: [
    GatewaySharedModule,
    RouterModule.forChild(notificationRoute),
    BrowserModule,
    HttpClientModule,
  ],
  declarations: [NotificationComponent, ],
})
export class NotificationsNotificationModule {}
