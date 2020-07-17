import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlane } from 'app/shared/model/flights/plane.model';
import { PlaneService } from './plane.service';

@Component({
  templateUrl: './plane-delete-dialog.component.html',
})
export class PlaneDeleteDialogComponent {
  plane?: IPlane;

  constructor(protected planeService: PlaneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('planeListModification');
      this.activeModal.close();
    });
  }
}
