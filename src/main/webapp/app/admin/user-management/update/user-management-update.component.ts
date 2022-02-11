import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { LANGUAGES } from 'app/config/language.constants';
import { User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {createMask} from "@ngneat/input-mask";
import {DataUtils, FileLoadError} from "../../../core/util/data-util.service";
import {EventManager, EventWithContent} from "../../../core/util/event-manager.service";
import {AlertError} from "../../../shared/alert/alert-error.model";
import {MatDialog} from "@angular/material/dialog";
import {NzModalService} from "ng-zorro-antd/modal";
import {IPersonne, Personne} from "../../../entities/personne/personne.model";

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
})
export class UserManagementUpdateComponent implements OnInit {
  user?: User;
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
  licenseInputMask = createMask('****-****-****-****');
  telephoneInputMask = createMask('99-99-99-99');
  dateInputMask = createMask<Date>({
    alias: 'datetime',
    inputFormat: 'dd/mm/yyyy',
    parser: (value: string) => {
      const values = value.split('/');
      const year = +values[2];
      const month = +values[1] - 1;
      const date = +values[0];
      return new Date(year, month, date);
    },
  });

  editForm = this.fb.group({
    id: [],
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    activated: [],
    langKey: [],
    authorities: [],

    nom: [],
    prenom: [],
    dataNaissance: [],
    lieuNaissance: [],
    numeroDoc: [],
    profil: [],
    profilContentType: [],
    telephone: [],
    user: [],
    structure: [],
  });

  constructor(
    private userService: UserManagementService,
    private route: ActivatedRoute,
    private activeModal: NgbActiveModal,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    private dialog: MatDialog,
    private modal: NzModalService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.route.data.subscribe(({ user }) => {
    //   if (user) {
    //     this.user = user;
    //     if (this.user.id === undefined) {
    //       this.user.activated = true;
    //     }
    //
    //   }
    // });

    if (this.user){
      this.updateForm(this.user);
    }

    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    // window.history.back();
    this.activeModal.close();
  }

  save(): void {
    this.isSaving = true;
    this.updateUser(this.user!);
    if (this.user!.id !== undefined) {
      this.userService.update(this.user!).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.userService.create(this.user!).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('appPharmaApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  success(msg: string): void{
    this.modal.success({
      nzContent: msg,
      nzTitle: 'SUCCESS',
      nzOkText: 'OK'
    })
  }
  warning(msg: string): void{
    this.modal.warning({
      nzContent: msg,
      nzTitle: 'ATTENTION',
      nzOkText: 'OK'
    })
  }
  error(msg: string): void{
    this.modal.error({
      nzContent: msg,
      nzTitle: 'ERROR',
      nzOkText: 'OK'
    })
  }

  protected createFromFormPresonne(): IPersonne {
    return {
      ...new Personne(),
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dataNaissance: this.editForm.get(['dataNaissance'])!.value,
      numeroDoc: this.editForm.get(['numeroDoc'])!.value,
      profilContentType: this.editForm.get(['profilContentType'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
    };
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
    });
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login'])!.value;
    user.firstName = this.editForm.get(['firstName'])!.value;
    user.lastName = this.editForm.get(['lastName'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.langKey = this.editForm.get(['langKey'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
