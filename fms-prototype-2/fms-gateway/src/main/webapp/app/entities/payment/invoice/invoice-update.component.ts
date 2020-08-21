import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';

import {IInvoice, Invoice} from 'app/shared/model/payment/invoice.model';
import {InvoiceService} from './invoice.service';
import {NotificationService} from "app/entities/notifications/notification/notification.service";
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  providers: [NotificationService,]
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    invoiceNumber: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    passengerId: [null, [Validators.required]],
    bookingNumber: [null, [Validators.required]],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);
    });
  }

  updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      invoiceNumber: Math.floor(Math.random() * (9999 - 1000)) + 1000,
      amount: this.activatedRoute.snapshot.paramMap.get('price'),
      passengerId: invoice.passengerId,
      bookingNumber: this.activatedRoute.snapshot.paramMap.get('bookingNumber'),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  private createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      passengerId: this.editForm.get(['passengerId'])!.value,
      bookingNumber: this.editForm.get(['bookingNumber'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  public onSaveSuccess(): void {
    this.isSaving = false;
    this.notificationService.getNotification(ENotificationType.BOOKING_CONFIRMED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          window.confirm(notification.description);
        } else {
          window.confirm("undefined");
        }
      });
    window.history.go(-4);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

}
