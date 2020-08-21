import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPayment, Payment } from 'app/shared/model/payment/payment.model';
import { PaymentService } from './payment.service';
import { IInvoice } from 'app/shared/model/payment/invoice.model';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    passengerId: [null, [Validators.required]],
    toPay: [null, [Validators.required]],
    bookingNumber: [null, [Validators.required]],
  });

  constructor(
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      passengerId: payment.passengerId,
      toPay: this.activatedRoute.snapshot.paramMap.get('fprice'),
      bookingNumber: this.activatedRoute.snapshot.paramMap.get('bookingNumber'),
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
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  public onSaveSuccess(): void {
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IInvoice): any {
    return item.id;
  }

  public getBookingNumber(): number {
    return this.editForm.get(['bookingNumber'])!.value;
  }

  public getFlightPrice(): string {
    return this.editForm.get(['toPay'])!.value;
  }
}
