import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IChatroom, Chatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-chatroom-update',
  templateUrl: './chatroom-update.component.html',
})
export class ChatroomUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    chatroomName: [],
    members: [],
  });

  constructor(
    protected chatroomService: ChatroomService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatroom }) => {
      this.updateForm(chatroom);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(chatroom: IChatroom): void {
    this.editForm.patchValue({
      id: chatroom.id,
      chatroomName: chatroom.chatroomName,
      members: chatroom.members,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatroom = this.createFromForm();
    if (chatroom.id !== undefined) {
      this.subscribeToSaveResponse(this.chatroomService.update(chatroom));
    } else {
      this.subscribeToSaveResponse(this.chatroomService.create(chatroom));
    }
  }

  private createFromForm(): IChatroom {
    return {
      ...new Chatroom(),
      id: this.editForm.get(['id'])!.value,
      chatroomName: this.editForm.get(['chatroomName'])!.value,
      members: this.editForm.get(['members'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatroom>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
