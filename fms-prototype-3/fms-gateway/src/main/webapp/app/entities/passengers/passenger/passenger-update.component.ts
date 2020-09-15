import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPassenger, Passenger } from 'app/shared/model/passengers/passenger.model';
import { PassengerService } from './passenger.service';

@Component({
  selector: 'jhi-passenger-update',
  templateUrl: './passenger-update.component.html',
})
export class PassengerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    name: [null, [Validators.required]],
    surname: [null, [Validators.required]],
    email: [null, [Validators.required]],
  });

  constructor(protected passengerService: PassengerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passenger }) => {
      this.updateForm(passenger);
    });
  }

  updateForm(passenger: IPassenger): void {
    this.editForm.patchValue({
      id: passenger.id,
      username: passenger.username,
      password: passenger.password,
      name: passenger.name,
      surname: passenger.surname,
      email: passenger.email,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passenger = this.createFromForm();
    if (passenger.id !== undefined) {
      this.subscribeToSaveResponse(this.passengerService.update(passenger));
    } else {
      this.subscribeToSaveResponse(this.passengerService.create(passenger));
    }
  }

  private createFromForm(): IPassenger {
    return {
      ...new Passenger(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassenger>>): void {
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
