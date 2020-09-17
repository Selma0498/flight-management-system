import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICreditCard } from 'app/shared/model/payments/credit-card.model';
import { CreditCardService } from './credit-card.service';

@Component({
  templateUrl: './credit-card-delete-dialog.component.html',
})
export class CreditCardDeleteDialogComponent {
  creditCard?: ICreditCard;

  constructor(
    protected creditCardService: CreditCardService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.creditCardService.delete(id).subscribe(() => {
      this.eventManager.broadcast('creditCardListModification');
      this.activeModal.close();
    });
  }
}
