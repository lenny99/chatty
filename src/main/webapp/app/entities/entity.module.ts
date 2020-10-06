import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'chatroom',
        loadChildren: () => import('./chatroom/chatroom.module').then(m => m.ChattyChatroomModule),
      },
      {
        path: 'message',
        loadChildren: () => import('./message/message.module').then(m => m.ChattyMessageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ChattyEntityModule {}
