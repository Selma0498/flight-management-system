import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFlight, Flight } from 'app/shared/model/flights/flight.model';
import { FlightService } from './flight.service';
import { IAirport } from 'app/shared/model/flights/airport.model';
import { AirportService } from 'app/entities/flights/airport/airport.service';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";
import {NotificationService} from "app/entities/notifications/notification/notification.service";
import {NotificationRepoService} from "app/entities/passengers/notification-repo/notification-repo.service";

@Component({
  selector: 'jhi-flight-update',
  templateUrl: './flight-update.component.html',
})
export class FlightUpdateComponent implements OnInit {
  isSaving = false;
  airports: IAirport[] = [];
  departureDateDp: any;

  editForm = this.fb.group({
    id: [],
    flightNumber: [null, [Validators.required]],
    flightType: [null, [Validators.required]],
    fareType: [null, [Validators.required]],
    pilot: [],
    planeModelNumber: [],
    price: [null, [Validators.required]],
    departureDate: [null, [Validators.required]],
    boardingGate: [null, [Validators.required]],
    airlineName: [],
    origin: [null, Validators.required],
    destination: [null, Validators.required],
  });

  constructor(
    protected flightService: FlightService,
    protected airportService: AirportService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private notificationRepoService: NotificationRepoService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flight }) => {
      this.updateForm(flight);

      this.airportService.query().subscribe((res: HttpResponse<IAirport[]>) => (this.airports = res.body || []));
    });
  }

  updateForm(flight: IFlight): void {
    this.editForm.patchValue({
      id: flight.id,
      flightNumber: flight.flightNumber,
      flightType: flight.flightType,
      fareType: flight.fareType,
      pilot: flight.pilot,
      planeModelNumber: flight.planeModelNumber,
      price: flight.price,
      departureDate: flight.departureDate,
      boardingGate: flight.boardingGate,
      airlineName: flight.airlineName,
      origin: flight.origin,
      destination: flight.destination,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flight = this.createFromForm();
    if (flight.id !== undefined) {
      this.subscribeToSaveResponse(this.flightService.update(flight));
    } else {
      this.subscribeToSaveResponse(this.flightService.create(flight));
    }
  }

  private createFromForm(): IFlight {
    return {
      ...new Flight(),
      id: this.editForm.get(['id'])!.value,
      flightNumber: this.editForm.get(['flightNumber'])!.value,
      flightType: this.editForm.get(['flightType'])!.value,
      fareType: this.editForm.get(['fareType'])!.value,
      pilot: this.editForm.get(['pilot'])!.value,
      planeModelNumber: this.editForm.get(['planeModelNumber'])!.value,
      price: this.editForm.get(['price'])!.value,
      departureDate: this.editForm.get(['departureDate'])!.value,
      boardingGate: this.editForm.get(['boardingGate'])!.value,
      airlineName: this.editForm.get(['airlineName'])!.value,
      origin: this.editForm.get(['origin'])!.value,
      destination: this.editForm.get(['destination'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlight>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    //save notification in notification repo for passengers to see
    //save notification in notification repo for passengers to see
    this.notificationRepoService.updateNotificationRepo(ENotificationType.FLIGHT_CANCELLED, this.editForm.get(['flightNumber'])!.value);

    this.notificationService.getNotification(ENotificationType.FLIGHT_UPDATED)
      .subscribe(notification => {
        if(notification.description !== undefined) {
          window.confirm(notification.description);
        } else {
          window.confirm("undefined");
        }
      });
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IAirport): any {
    return item.id;
  }
}
