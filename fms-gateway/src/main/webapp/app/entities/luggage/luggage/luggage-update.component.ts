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
    role: [null, [Validators.required]],
    luggageNumber: [null, [Validators.required]],
    flightNumber: [null, [Validators.required]],
    passengerId: [null, [Validators.required]],
    weight: [null, [Validators.required]],
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
      role: luggage.role,
      luggageNumber: luggage.luggageNumber,
      flightNumber: luggage.flightNumber,
      passengerId: luggage.passengerId,
      weight: luggage.weight,
      rfidTag: luggage.rfidTag,
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
      role: this.editForm.get(['role'])!.value,
      luggageNumber: this.editForm.get(['luggageNumber'])!.value,
      flightNumber: this.editForm.get(['flightNumber'])!.value,
      passengerId: this.editForm.get(['passengerId'])!.value,
      weight: this.editForm.get(['weight'])!.value,
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
}
