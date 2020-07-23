import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBooking, Booking } from 'app/shared/model/bookings/booking.model';
import { BookingService } from './booking.service';

@Component({
  selector: 'jhi-booking-update',
  templateUrl: './booking-update.component.html',
})
export class BookingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bookingNumber: [null, [Validators.required]],
    flightNumber: [null, [Validators.required]],
    passengerId: [null, [Validators.required]],
    invoiceNumber: [],
    invoiceSet: [null, [Validators.required]],
    state: [null, [Validators.required]],
  });

  constructor(protected bookingService: BookingService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booking }) => {
      this.updateForm(booking);
    });
  }

  updateForm(booking: IBooking): void {
    this.editForm.patchValue({
      id: booking.id,
      bookingNumber: booking.bookingNumber,
      flightNumber: booking.flightNumber,
      passengerId: booking.passengerId,
      invoiceNumber: booking.invoiceNumber,
      invoiceSet: booking.invoiceSet,
      state: booking.state,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booking = this.createFromForm();
    if (booking.id !== undefined) {
      this.subscribeToSaveResponse(this.bookingService.update(booking));
    } else {
      this.subscribeToSaveResponse(this.bookingService.create(booking));
    }
  }

  private createFromForm(): IBooking {
    return {
      ...new Booking(),
      id: this.editForm.get(['id'])!.value,
      bookingNumber: this.editForm.get(['bookingNumber'])!.value,
      flightNumber: this.editForm.get(['flightNumber'])!.value,
      passengerId: this.editForm.get(['passengerId'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceSet: this.editForm.get(['invoiceSet'])!.value,
      state: this.editForm.get(['state'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>): void {
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
}
