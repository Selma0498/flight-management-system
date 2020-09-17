import { INotificationRepo } from 'app/shared/model/passengers/notification-repo.model';

export interface IPassenger {
  id?: number;
  username?: string;
  password?: string;
  name?: string;
  surname?: string;
  email?: string;
  notificationRepos?: INotificationRepo[];
}

export class Passenger implements IPassenger {
  constructor(
    public id?: number,
    public username?: string,
    public password?: string,
    public name?: string,
    public surname?: string,
    public email?: string,
    public notificationRepos?: INotificationRepo[]
  ) {}
}
