import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { CreditCardComponent } from './credit-card.component';
import { CreditCardDetailComponent } from './credit-card-detail.component';
import { CreditCardUpdateComponent } from './credit-card-update.component';
import { CreditCardDeleteDialogComponent } from './credit-card-delete-dialog.component';
import { creditCardRoute } from './credit-card.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(creditCardRoute)],
  declarations: [CreditCardComponent, CreditCardDetailComponent, CreditCardUpdateComponent, CreditCardDeleteDialogComponent],
  entryComponents: [CreditCardDeleteDialogComponent],
})
export class PaymentsCreditCardModule {}
