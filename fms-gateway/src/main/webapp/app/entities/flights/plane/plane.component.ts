import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlane } from 'app/shared/model/flights/plane.model';
import { PlaneService } from './plane.service';
import { PlaneDeleteDialogComponent } from './plane-delete-dialog.component';

@Component({
  selector: 'jhi-plane',
  templateUrl: './plane.component.html',
})
export class PlaneComponent implements OnInit, OnDestroy {
  planes?: IPlane[];
  eventSubscriber?: Subscription;

  constructor(protected planeService: PlaneService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.planeService.query().subscribe((res: HttpResponse<IPlane[]>) => (this.planes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPlanes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlane): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPlanes(): void {
    this.eventSubscriber = this.eventManager.subscribe('planeListModification', () => this.loadAll());
  }

  delete(plane: IPlane): void {
    const modalRef = this.modalService.open(PlaneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plane = plane;
  }
}
