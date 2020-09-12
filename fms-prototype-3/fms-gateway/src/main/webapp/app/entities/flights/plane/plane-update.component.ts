import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlane, Plane } from 'app/shared/model/flights/plane.model';
import { PlaneService } from './plane.service';

@Component({
  selector: 'jhi-plane-update',
  templateUrl: './plane-update.component.html',
})
export class PlaneUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    manufacturer: [],
    modelNumber: [],
    registrationNumber: [null, [Validators.required]],
  });

  constructor(protected planeService: PlaneService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plane }) => {
      this.updateForm(plane);
    });
  }

  updateForm(plane: IPlane): void {
    this.editForm.patchValue({
      id: plane.id,
      manufacturer: plane.manufacturer,
      modelNumber: plane.modelNumber,
      registrationNumber: plane.registrationNumber,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plane = this.createFromForm();
    if (plane.id !== undefined) {
      this.subscribeToSaveResponse(this.planeService.update(plane));
    } else {
      this.subscribeToSaveResponse(this.planeService.create(plane));
    }
  }

  private createFromForm(): IPlane {
    return {
      ...new Plane(),
      id: this.editForm.get(['id'])!.value,
      manufacturer: this.editForm.get(['manufacturer'])!.value,
      modelNumber: this.editForm.get(['modelNumber'])!.value,
      registrationNumber: this.editForm.get(['registrationNumber'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlane>>): void {
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
