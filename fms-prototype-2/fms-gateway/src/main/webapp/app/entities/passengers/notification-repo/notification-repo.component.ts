import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INotificationRepo } from 'app/shared/model/passengers/notification-repo.model';
import { NotificationRepoService } from './notification-repo.service';

@Component({
  selector: 'jhi-notification-repo',
  templateUrl: './notification-repo.component.html',
})
export class NotificationRepoComponent implements OnInit, OnDestroy {
  notificationRepos?: INotificationRepo[];
  eventSubscriber?: Subscription;

  constructor(
    protected notificationRepoService: NotificationRepoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.notificationRepoService.query().subscribe((res: HttpResponse<INotificationRepo[]>) => (this.notificationRepos = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInNotificationRepos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: INotificationRepo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInNotificationRepos(): void {
    this.eventSubscriber = this.eventManager.subscribe('notificationRepoListModification', () => this.loadAll());
  }

}
