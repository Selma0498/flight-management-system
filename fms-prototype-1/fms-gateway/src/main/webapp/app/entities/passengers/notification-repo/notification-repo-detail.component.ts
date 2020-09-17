import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

@Component({
  selector: 'jhi-notification-repo-detail',
  templateUrl: './notification-repo-detail.component.html',
})
export class NotificationRepoDetailComponent implements OnInit {
  notificationRepo: INotificationRepo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationRepo }) => (this.notificationRepo = notificationRepo));
  }

  previousState(): void {
    window.history.back();
  }
}
