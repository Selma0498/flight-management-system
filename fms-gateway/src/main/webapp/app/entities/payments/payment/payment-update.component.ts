import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPayment, Payment } from 'app/shared/model/payments/payment.model';
import { PaymentService } from './payment.service';
import { IInvoice } from 'app/shared/model/payments/invoice.model';
import { InvoiceService } from 'app/entities/payments/invoice/invoice.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  invoices: IInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    passengerId: [null, [Validators.required]],
    toPay: [null, [Validators.required]],
    invoice: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.invoiceService
        .query({ filter: 'payment-is-null' })
        .pipe(
          map((res: HttpResponse<IInvoice[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IInvoice[]) => {
          if (!payment.invoice || !payment.invoice.id) {
            this.invoices = resBody;
          } else {
            this.invoiceService
              .find(payment.invoice.id)
              .pipe(
                map((subRes: HttpResponse<IInvoice>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IInvoice[]) => (this.invoices = concatRes));
          }
        });
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      passengerId: payment.passengerId,
      toPay: payment.toPay,
      invoice: payment.invoice,
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
      invoice: this.editForm.get(['invoice'])!.value,
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
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IInvoice): any {
    return item.id;
  }
}
