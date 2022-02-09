import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;

  personnesSharedCollection: IPersonne[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [],
    content: [],
    dateNotif: [],
    vue: [],
    operateur: [],
  });

  constructor(
    protected notificationService: NotificationService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (notification.id === undefined) {
        const today = dayjs().startOf('day');
        notification.dateNotif = today;
      }

      this.updateForm(notification);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  trackPersonneById(index: number, item: IPersonne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  protected updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      titre: notification.titre,
      content: notification.content,
      dateNotif: notification.dateNotif ? notification.dateNotif.format(DATE_TIME_FORMAT) : null,
      vue: notification.vue,
      operateur: notification.operateur,
    });

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing(
      this.personnesSharedCollection,
      notification.operateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing(personnes, this.editForm.get('operateur')!.value)
        )
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }

  protected createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      content: this.editForm.get(['content'])!.value,
      dateNotif: this.editForm.get(['dateNotif'])!.value ? dayjs(this.editForm.get(['dateNotif'])!.value, DATE_TIME_FORMAT) : undefined,
      vue: this.editForm.get(['vue'])!.value,
      operateur: this.editForm.get(['operateur'])!.value,
    };
  }
}
