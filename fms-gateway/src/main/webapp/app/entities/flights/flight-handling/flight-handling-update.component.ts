import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFlightHandling, FlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { FlightHandlingService } from './flight-handling.service';

@Component({
  selector: 'jhi-flight-handling-update',
  templateUrl: './flight-handling-update.component.html',
})
export class FlightHandlingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    delay: [],
  });

  constructor(protected flightHandlingService: FlightHandlingService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flightHandling }) => {
      this.updateForm(flightHandling);
    });
  }

  updateForm(flightHandling: IFlightHandling): void {
    this.editForm.patchValue({
      id: flightHandling.id,
      delay: flightHandling.delay,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flightHandling = this.createFromForm();
    if (flightHandling.id !== undefined) {
      this.subscribeToSaveResponse(this.flightHandlingService.update(flightHandling));
    } else {
      this.subscribeToSaveResponse(this.flightHandlingService.create(flightHandling));
    }
  }

  private createFromForm(): IFlightHandling {
    return {
      ...new FlightHandling(),
      id: this.editForm.get(['id'])!.value,
      delay: this.editForm.get(['delay'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlightHandling>>): void {
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
