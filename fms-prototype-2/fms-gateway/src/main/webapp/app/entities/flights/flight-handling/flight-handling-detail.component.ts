import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFlightHandling } from 'app/shared/model/flights/flight-handling.model';

@Component({
  selector: 'jhi-flight-handling-detail',
  templateUrl: './flight-handling-detail.component.html',
})
export class FlightHandlingDetailComponent implements OnInit {
  flightHandling: IFlightHandling | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flightHandling }) => (this.flightHandling = flightHandling));
  }

  previousState(): void {
    window.history.back();
  }
}
