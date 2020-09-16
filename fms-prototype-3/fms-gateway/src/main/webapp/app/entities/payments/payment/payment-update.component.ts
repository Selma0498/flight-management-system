import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPayment, Payment } from 'app/shared/model/payments/payment.model';
import { PaymentService } from './payment.service';
import { ICreditCard } from 'app/shared/model/payments/credit-card.model';
import { CreditCardService } from 'app/entities/payments/credit-card/credit-card.service';
import {NotificationService} from "app/entities/notifications/notification/notification.service";
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  providers: [NotificationService,]
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  creditcards: ICreditCard[] = [];

  editForm = this.fb.group({
    id: [],
    passengerId: [null, [Validators.required]],
    toPay: [null, [Validators.required]],
    bookingNumber: [null, [Validators.required]],
    creditCard: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected creditCardService: CreditCardService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.creditCardService
        .query({ filter: 'payment-is-null' })
        .pipe(
          map((res: HttpResponse<ICreditCard[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICreditCard[]) => {
          if (!payment.creditCard || !payment.creditCard.id) {
            this.creditcards = resBody;
          } else {
            this.creditCardService
              .find(payment.creditCard.id)
              .pipe(
                map((subRes: HttpResponse<ICreditCard>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICreditCard[]) => (this.creditcards = concatRes));
          }
        });
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      passengerId: payment.passengerId,
      toPay: this.activatedRoute.snapshot.paramMap.get('fprice'),
      bookingNumber: this.activatedRoute.snapshot.paramMap.get('bookingNumber'),
      creditCard: payment.creditCard,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      passengerId: this.editForm.get(['passengerId'])!.value,
      toPay: this.editForm.get(['toPay'])!.value,
      bookingNumber: this.editForm.get(['bookingNumber'])!.value,
      creditCard: this.editForm.get(['creditCard'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.notificationService.getNotification(ENotificationType.BOOKING_CONFIRMED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          window.confirm(notification.description);
        } else {
          window.confirm("undefined");
        }
      });
    window.history.go(-3);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICreditCard): any {
    return item.id;
  }
}
