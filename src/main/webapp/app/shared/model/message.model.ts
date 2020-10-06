import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IMessage {
  id?: number;
  content?: string;
  timeSend?: Moment;
  user?: IUser;
}

export class Message implements IMessage {
  constructor(public id?: number, public content?: string, public timeSend?: Moment, public user?: IUser) {}
}
