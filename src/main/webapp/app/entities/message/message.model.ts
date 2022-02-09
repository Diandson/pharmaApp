import dayjs from 'dayjs/esm';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IMessage {
  id?: number;
  message?: string | null;
  dateMessage?: dayjs.Dayjs | null;
  to?: string | null;
  vue?: boolean | null;
  from?: IPersonne | null;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public message?: string | null,
    public dateMessage?: dayjs.Dayjs | null,
    public to?: string | null,
    public vue?: boolean | null,
    public from?: IPersonne | null
  ) {
    this.vue = this.vue ?? false;
  }
}

export function getMessageIdentifier(message: IMessage): number | undefined {
  return message.id;
}
