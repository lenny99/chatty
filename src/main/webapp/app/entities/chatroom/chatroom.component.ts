import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';
import { ChatroomDeleteDialogComponent } from './chatroom-delete-dialog.component';

@Component({
  selector: 'jhi-chatroom',
  templateUrl: './chatroom.component.html',
})
export class ChatroomComponent implements OnInit, OnDestroy {
  chatrooms?: IChatroom[];
  eventSubscriber?: Subscription;

  constructor(protected chatroomService: ChatroomService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.chatroomService.query().subscribe((res: HttpResponse<IChatroom[]>) => (this.chatrooms = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInChatrooms();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IChatroom): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInChatrooms(): void {
    this.eventSubscriber = this.eventManager.subscribe('chatroomListModification', () => this.loadAll());
  }

  delete(chatroom: IChatroom): void {
    const modalRef = this.modalService.open(ChatroomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chatroom = chatroom;
  }
}
