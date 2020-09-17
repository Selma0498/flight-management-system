import { IPassenger } from 'app/shared/model/passengers/passenger.model';

export interface INotificationRepo {
  id?: number;
  name?: string;
  description?: string;
  passengers?: IPassenger[];
}

export class NotificationRepo implements INotificationRepo {
  constructor(public id?: number, public name?: string, public description?: string, public passengers?: IPassenger[]) {}
}
