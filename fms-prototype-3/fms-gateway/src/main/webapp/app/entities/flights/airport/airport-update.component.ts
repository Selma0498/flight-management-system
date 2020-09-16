import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAirport, Airport } from 'app/shared/model/flights/airport.model';
import { AirportService } from './airport.service';

@Component({
  selector: 'jhi-airport-update',
  templateUrl: './airport-update.component.html',
})
export class AirportUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    airportCode: [],
    airportName: [],
    countryName: [],
    cityName: [],
    postalCode: [],
  });

  constructor(protected airportService: AirportService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ airport }) => {
      this.updateForm(airport);
    });
  }

  updateForm(airport: IAirport): void {
    this.editForm.patchValue({
      id: airport.id,
      airportCode: airport.airportCode,
      airportName: airport.airportName,
      countryName: airport.countryName,
      cityName: airport.cityName,
      postalCode: airport.postalCode,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const airport = this.createFromForm();
    if (airport.id !== undefined) {
      this.subscribeToSaveResponse(this.airportService.update(airport));
    } else {
      this.subscribeToSaveResponse(this.airportService.create(airport));
    }
  }

  private createFromForm(): IAirport {
    return {
      ...new Airport(),
      id: this.editForm.get(['id'])!.value,
      airportCode: this.editForm.get(['airportCode'])!.value,
      airportName: this.editForm.get(['airportName'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      cityName: this.editForm.get(['cityName'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAirport>>): void {
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
