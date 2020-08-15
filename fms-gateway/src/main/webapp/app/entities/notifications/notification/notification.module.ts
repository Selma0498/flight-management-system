import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { GatewaySharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [
    GatewaySharedModule,
    BrowserModule,
    HttpClientModule,
  ],
})
export class NotificationsNotificationModule {}
