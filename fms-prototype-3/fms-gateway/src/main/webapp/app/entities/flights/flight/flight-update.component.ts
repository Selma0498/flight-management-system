import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IFlight, Flight } from 'app/shared/model/flights/flight.model';
import { FlightService } from './flight.service';
import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';
import { FlightHandlingService } from 'app/entities/flights/flight-handling/flight-handling.service';
import { IAirport } from 'app/shared/model/flights/airport.model';
import { AirportService } from 'app/entities/flights/airport/airport.service';
import { IAirline } from 'app/shared/model/flights/airline.model';
import { AirlineService } from 'app/entities/flights/airline/airline.service';
import { IPlane } from 'app/shared/model/flights/plane.model';
import { PlaneService } from 'app/entities/flights/plane/plane.service';
import {ENotificationType} from "app/shared/model/enumerations/e-notification-type.model";
import {NotificationService} from "app/entities/notifications/notification/notification.service";

type SelectableEntity = IFlightHandling | IAirport | IAirline | IPlane;

@Component({
  selector: 'jhi-flight-update',
  templateUrl: './flight-update.component.html',
})
export class FlightUpdateComponent implements OnInit {
  isSaving = false;
  flighthandlers: IFlightHandling[] = [];
  airports: IAirport[] = [];
  airlines: IAirline[] = [];
  planes: IPlane[] = [];

  editForm = this.fb.group({
    id: [],
    flightNumber: [null, [Validators.required]],
    flightType: [null, [Validators.required]],
    fareType: [null, [Validators.required]],
    pilot: [],
    price: [null, [Validators.required]],
    flightHandler: [],
    origin: [null, Validators.required],
    destination: [null, Validators.required],
    airline: [null, Validators.required],
    plane: [null, Validators.required],
  });

  constructor(
    protected flightService: FlightService,
    protected flightHandlingService: FlightHandlingService,
    protected airportService: AirportService,
    protected airlineService: AirlineService,
    protected planeService: PlaneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flight }) => {
      this.updateForm(flight);

      this.flightHandlingService
        .query({ filter: 'flight-is-null' })
        .pipe(
          map((res: HttpResponse<IFlightHandling[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IFlightHandling[]) => {
          if (!flight.flightHandler || !flight.flightHandler.id) {
            this.flighthandlers = resBody;
          } else {
            this.flightHandlingService
              .find(flight.flightHandler.id)
              .pipe(
                map((subRes: HttpResponse<IFlightHandling>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IFlightHandling[]) => (this.flighthandlers = concatRes));
          }
        });

      this.airportService.query().subscribe((res: HttpResponse<IAirport[]>) => (this.airports = res.body || []));

      this.airlineService.query().subscribe((res: HttpResponse<IAirline[]>) => (this.airlines = res.body || []));

      this.planeService.query().subscribe((res: HttpResponse<IPlane[]>) => (this.planes = res.body || []));
    });
  }

  updateForm(flight: IFlight): void {
    this.editForm.patchValue({
      id: flight.id,
      flightNumber: flight.flightNumber,
      flightType: flight.flightType,
      fareType: flight.fareType,
      pilot: flight.pilot,
      price: flight.price,
      flightHandler: flight.flightHandler,
      origin: flight.origin,
      destination: flight.destination,
      airline: flight.airline,
      plane: flight.plane,
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
      price: this.editForm.get(['price'])!.value,
      flightHandler: this.editForm.get(['flightHandler'])!.value,
      origin: this.editForm.get(['origin'])!.value,
      destination: this.editForm.get(['destination'])!.value,
      airline: this.editForm.get(['airline'])!.value,
      plane: this.editForm.get(['plane'])!.value,
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
