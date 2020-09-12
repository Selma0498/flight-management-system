import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILuggage } from 'app/shared/model/luggage/luggage.model';
import { LuggageService } from './luggage.service';
import { LuggageDeleteDialogComponent } from './luggage-delete-dialog.component';

@Component({
  selector: 'jhi-luggage',
  templateUrl: './luggage.component.html',
})
export class LuggageComponent implements OnInit, OnDestroy {
  luggages?: ILuggage[];
  eventSubscriber?: Subscription;

  constructor(protected luggageService: LuggageService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.luggageService.query().subscribe((res: HttpResponse<ILuggage[]>) => (this.luggages = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLuggages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILuggage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLuggages(): void {
    this.eventSubscriber = this.eventManager.subscribe('luggageListModification', () => this.loadAll());
  }

  delete(luggage: ILuggage): void {
    const modalRef = this.modalService.open(LuggageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.luggage = luggage;
  }
}
