import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMessage, Message } from '../message.model';
import { MessageService } from '../service/message.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.component.html',
})
export class MessageUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    message: [],
    dateMessage: [],
    to: [],
    vue: [],
    from: [],
  });

  constructor(
    protected messageService: MessageService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ message }) => {
      if (message.id === undefined) {
        const today = dayjs().startOf('day');
        message.dateMessage = today;
      }

      this.updateForm(message);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.id !== undefined) {
      this.subscribeToSaveResponse(this.messageService.update(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.create(message));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(message: IMessage): void {
    this.editForm.patchValue({
      id: message.id,
      message: message.message,
      dateMessage: message.dateMessage ? message.dateMessage.format(DATE_TIME_FORMAT) : null,
      to: message.to,
      vue: message.vue,
      from: message.from,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(this.personnesSharedCollection, message.from);
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) => this.personneService.addPersonneToCollectionIfMissing(personnes, this.editForm.get('from')!.value))
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }

  protected createFromForm(): IMessage {
    return {
      ...new Message(),
      id: this.editForm.get(['id'])!.value,
      message: this.editForm.get(['message'])!.value,
      dateMessage: this.editForm.get(['dateMessage'])!.value
        ? dayjs(this.editForm.get(['dateMessage'])!.value, DATE_TIME_FORMAT)
        : undefined,
      to: this.editForm.get(['to'])!.value,
      vue: this.editForm.get(['vue'])!.value,
      from: this.editForm.get(['from'])!.value,
    };
  }
}
