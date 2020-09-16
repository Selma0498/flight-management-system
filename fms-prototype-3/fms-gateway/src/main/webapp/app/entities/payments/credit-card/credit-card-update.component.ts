import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICreditCard, CreditCard } from 'app/shared/model/payments/credit-card.model';
import { CreditCardService } from './credit-card.service';

@Component({
  selector: 'jhi-credit-card-update',
  templateUrl: './credit-card-update.component.html',
})
export class CreditCardUpdateComponent implements OnInit {
  isSaving = false;
  validityDateDp: any;

  editForm = this.fb.group({
    id: [],
    cardType: [null, [Validators.required]],
    cvc: [null, [Validators.required]],
    cardNumber: [null, [Validators.required]],
    validityDate: [null, [Validators.required]],
  });

  constructor(protected creditCardService: CreditCardService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCard }) => {
      this.updateForm(creditCard);
    });
  }

  updateForm(creditCard: ICreditCard): void {
    this.editForm.patchValue({
      id: creditCard.id,
      cardType: creditCard.cardType,
      cvc: creditCard.cvc,
      cardNumber: creditCard.cardNumber,
      validityDate: creditCard.validityDate,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const creditCard = this.createFromForm();
    if (creditCard.id !== undefined) {
      this.subscribeToSaveResponse(this.creditCardService.update(creditCard));
    } else {
      this.subscribeToSaveResponse(this.creditCardService.create(creditCard));
    }
  }

  private createFromForm(): ICreditCard {
    return {
      ...new CreditCard(),
      id: this.editForm.get(['id'])!.value,
      cardType: this.editForm.get(['cardType'])!.value,
      cvc: this.editForm.get(['cvc'])!.value,
      cardNumber: this.editForm.get(['cardNumber'])!.value,
      validityDate: this.editForm.get(['validityDate'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICreditCard>>): void {
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
