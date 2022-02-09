import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface INotification {
  id?: number;
  titre?: string | null;
  content?: string | null;
  dateNotif?: dayjs.Dayjs | null;
  vue?: boolean | null;
  operateur?: IPersonne | null;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public titre?: string | null,
    public content?: string | null,
    public dateNotif?: dayjs.Dayjs | null,
    public vue?: boolean | null,
    public operateur?: IPersonne | null
  ) {
    this.vue = this.vue ?? false;
  }
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
