import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { INotificationRepo, NotificationRepo } from 'app/shared/model/passengers/notification-repo.model';
import { NotificationRepoService } from './notification-repo.service';
import { IPassenger } from 'app/shared/model/passengers/passenger.model';
import { PassengerService } from 'app/entities/passengers/passenger/passenger.service';

@Component({
  selector: 'jhi-notification-repo-update',
  templateUrl: './notification-repo-update.component.html',
})
export class NotificationRepoUpdateComponent implements OnInit {
  isSaving = false;
  passengers: IPassenger[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    passengers: [],
  });

  constructor(
    protected notificationRepoService: NotificationRepoService,
    protected passengerService: PassengerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationRepo }) => {
      this.updateForm(notificationRepo);

      this.passengerService.query().subscribe((res: HttpResponse<IPassenger[]>) => (this.passengers = res.body || []));
    });
  }

  updateForm(notificationRepo: INotificationRepo): void {
    this.editForm.patchValue({
      id: notificationRepo.id,
      name: notificationRepo.name,
      description: notificationRepo.description,
      passengers: notificationRepo.passengers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificationRepo = this.createFromForm();
    if (notificationRepo.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationRepoService.update(notificationRepo));
    } else {
      this.subscribeToSaveResponse(this.notificationRepoService.create(notificationRepo));
    }
  }

  private createFromForm(): INotificationRepo {
    return {
      ...new NotificationRepo(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      passengers: this.editForm.get(['passengers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationRepo>>): void {
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

  trackById(index: number, item: IPassenger): any {
    return item.id;
  }

  getSelected(selectedVals: IPassenger[], option: IPassenger): IPassenger {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
