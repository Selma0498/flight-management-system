import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAirline } from 'app/shared/model/flights/airline.model';

@Component({
  selector: 'jhi-airline-detail',
  templateUrl: './airline-detail.component.html',
})
export class AirlineDetailComponent implements OnInit {
  airline: IAirline | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ airline }) => (this.airline = airline));
  }

  previousState(): void {
    window.history.back();
  }
}
