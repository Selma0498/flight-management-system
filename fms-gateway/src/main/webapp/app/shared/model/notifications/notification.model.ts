import { ENotificationType } from 'app/shared/model/enumerations/e-notification-type.model';

export interface INotification {
  id?: number;
  type?: ENotificationType;
  description?: string;
}

export class Notification implements INotification {
  constructor(public id?: number, public type?: ENotificationType, public description?: string) {}
}
