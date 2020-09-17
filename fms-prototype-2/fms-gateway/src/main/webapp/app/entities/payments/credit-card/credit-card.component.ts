import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICreditCard } from 'app/shared/model/payments/credit-card.model';
import { CreditCardService } from './credit-card.service';
import { CreditCardDeleteDialogComponent } from './credit-card-delete-dialog.component';

@Component({
  selector: 'jhi-credit-card',
  templateUrl: './credit-card.component.html',
})
export class CreditCardComponent implements OnInit, OnDestroy {
  creditCards?: ICreditCard[];
  eventSubscriber?: Subscription;

  constructor(protected creditCardService: CreditCardService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.creditCardService.query().subscribe((res: HttpResponse<ICreditCard[]>) => (this.creditCards = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCreditCards();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICreditCard): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCreditCards(): void {
    this.eventSubscriber = this.eventManager.subscribe('creditCardListModification', () => this.loadAll());
  }

  delete(creditCard: ICreditCard): void {
    const modalRef = this.modalService.open(CreditCardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.creditCard = creditCard;
  }
}
