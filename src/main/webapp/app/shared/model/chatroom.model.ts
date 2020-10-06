import { IUser } from 'app/core/user/user.model';

export interface IChatroom {
  id?: number;
  chatroomName?: string;
  members?: IUser[];
}

export class Chatroom implements IChatroom {
  constructor(public id?: number, public chatroomName?: string, public members?: IUser[]) {}
}
