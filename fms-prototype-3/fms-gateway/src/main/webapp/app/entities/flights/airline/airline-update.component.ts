import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAirline, Airline } from 'app/shared/model/flights/airline.model';
import { AirlineService } from './airline.service';

@Component({
  selector: 'jhi-airline-update',
  templateUrl: './airline-update.component.html',
})
export class AirlineUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    airlineName: [],
  });

  constructor(protected airlineService: AirlineService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ airline }) => {
      this.updateForm(airline);
    });
  }

  updateForm(airline: IAirline): void {
    this.editForm.patchValue({
      id: airline.id,
      airlineName: airline.airlineName,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const airline = this.createFromForm();
    if (airline.id !== undefined) {
      this.subscribeToSaveResponse(this.airlineService.update(airline));
    } else {
      this.subscribeToSaveResponse(this.airlineService.create(airline));
    }
  }

  private createFromForm(): IAirline {
    return {
      ...new Airline(),
      id: this.editForm.get(['id'])!.value,
      airlineName: this.editForm.get(['airlineName'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAirline>>): void {
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
