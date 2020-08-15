import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILuggage, Luggage } from 'app/shared/model/luggage/luggage.model';
import { LuggageService } from './luggage.service';

@Component({
  selector: 'jhi-luggage-update',
  templateUrl: './luggage-update.component.html',
})
export class LuggageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    luggageType: [null, [Validators.required]],
    luggageNumber: [null, [Validators.required]],
    flightNumber: [null, [Validators.required]],
    bookingNumber: [null, [Validators.required]],
    passengerId: [null, [Validators.required]],
    weightCategory: [null, [Validators.required]],
    rfidTag: [],
  });

  constructor(protected luggageService: LuggageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ luggage }) => {
      this.updateForm(luggage);
    });
  }

  updateForm(luggage: ILuggage): void {
    this.editForm.patchValue({
      id: luggage.id,
      luggageType: luggage.luggageType,
      luggageNumber: Math.floor(Math.random() * (9999 - 1000)) + 1000,
      flightNumber: this.activatedRoute.snapshot.paramMap.get('flightNumber'),
      bookingNumber: this.activatedRoute.snapshot.paramMap.get('bookingNumber'),
      passengerId: luggage.passengerId,
      weightCategory: luggage.weightCategory,
      rfidTag: this.getRandomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890,./;[])(*&^%$#@!~"),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const luggage = this.createFromForm();
    if (luggage.id !== undefined) {
      this.subscribeToSaveResponse(this.luggageService.update(luggage));
    } else {
      this.subscribeToSaveResponse(this.luggageService.create(luggage));
    }
  }

  private createFromForm(): ILuggage {
    return {
      ...new Luggage(),
      id: this.editForm.get(['id'])!.value,
      luggageType: this.editForm.get(['luggageType'])!.value,
      luggageNumber: this.editForm.get(['luggageNumber'])!.value,
      flightNumber: this.editForm.get(['flightNumber'])!.value,
      bookingNumber: this.editForm.get(['bookingNumber'])!.value,
      passengerId: this.editForm.get(['passengerId'])!.value,
      weightCategory: this.editForm.get(['weightCategory'])!.value,
      rfidTag: this.editForm.get(['rfidTag'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILuggage>>): void {
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

  // Helper method to generate a random string
  private getRandomString(possible: string): string {
    const STRING_LEN = 20;
    let result = "";
    for (let i = 0; i < STRING_LEN; i++) {
      result += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return result;
  }

}
