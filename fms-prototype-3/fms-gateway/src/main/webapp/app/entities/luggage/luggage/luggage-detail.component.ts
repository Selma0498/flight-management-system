import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILuggage } from 'app/shared/model/luggage/luggage.model';

@Component({
  selector: 'jhi-luggage-detail',
  templateUrl: './luggage-detail.component.html',
})
export class LuggageDetailComponent implements OnInit {
  luggage: ILuggage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ luggage }) => (this.luggage = luggage));
  }

  previousState(): void {
    window.history.back();
  }
}
