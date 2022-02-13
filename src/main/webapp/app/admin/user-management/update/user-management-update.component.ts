import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { LANGUAGES } from 'app/config/language.constants';
import { User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {createMask} from "@ngneat/input-mask";
import {DataUtils, FileLoadError} from "../../../core/util/data-util.service";
import {EventManager, EventWithContent} from "../../../core/util/event-manager.service";
import {AlertError} from "../../../shared/alert/alert-error.model";
import {MatDialog} from "@angular/material/dialog";
import {NzModalService} from "ng-zorro-antd/modal";
import {IPersonne, Personne} from "../../../entities/personne/personne.model";
import {AccountService} from "../../../core/auth/account.service";
import {Authority} from "../../../config/authority.constants";
import {ProgressDialogComponent} from "../../../shared/progress-dialog/progress-dialog.component";

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
  styleUrls: ['../user.component.scss']
})
export class UserManagementUpdateComponent implements OnInit {
  user?: User;
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
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
  acteur = "";
  authoritiesSelected: string[] = [];

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
    private accountService: AccountService,
    private modalService: NgbModal,
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
    const dialogRef = this.modalService.open(ProgressDialogComponent,
      { backdrop: 'static', centered: true, windowClass: 'myCustomModalClass' });

    // this.isSaving = true;
    const user = this.updateUser();
    const userMaj = this.updateUserMaj();
    if (user.id !== undefined) {
      this.userService.update(userMaj).subscribe(res => {
          if (res.login) {
            this.success('Utilisateur mis à jour avec succès');
            dialogRef.close();
            this.activeModal.close('saved');
          } else {
            this.warning('Une erreur est survenue');
            dialogRef.close();
          }
        },
        () => {
          this.error('Veuillez verifiez votre connexion!');
          dialogRef.close();
        });
    } else {
      this.userService.create(user).subscribe(res => {
          if (res.login) {
            this.success('Utilisateur ajouté avec succès')
            dialogRef.close();
            this.activeModal.close('saved');
          } else {
            this.warning('Une erreur est survenue');
            dialogRef.close();
          }
        },
        () => {
          this.error('Veuillez verifiez votre connexion!');
          dialogRef.close();
        });
    }
  }

  setAdmin(): void{
    if (this.accountService.hasAnyAuthority(Authority.STRUCTURE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.STRUCTURE_ADMIN.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'ADMIN';
      }
    }else if (this.accountService.hasAnyAuthority(Authority.AGENCE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.AGENCE_ADMIN.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'ADMIN';
      }
    }
    else{
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.ADMIN.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'ADMIN';
      }
    }
  }
  setServeur(): void{
    if (this.accountService.hasAnyAuthority(Authority.STRUCTURE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.SERVEUR.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'SERVEUR';
      }
    }else if (this.accountService.hasAnyAuthority(Authority.AGENCE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.SERVEUR.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'SERVEUR';
      }
    }
  }
  setCaisse(): void{
    if (this.accountService.hasAnyAuthority(Authority.STRUCTURE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.STRUCTURE_CAISSE.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'CAISSE';
      }
    }else if (this.accountService.hasAnyAuthority(Authority.AGENCE_ADMIN)){
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.USER.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes(Authority.STRUCTURE_CAISSE.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
        this.acteur = 'CAISSE';
      }
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

  private updateUser(): User {
    return {
      ...new User(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['nom'])!.value,
      lastName: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      langKey: this.editForm.get(['langKey'])!.value,
      authorities: this.authoritiesSelected,
      personne: this.createFromFormPresonne()
    }
  }
  private updateUserMaj(): User {
    return {
      ...new User(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      langKey: this.editForm.get(['langKey'])!.value,
      authorities: this.editForm.get(['authorities'])!.value,
    }
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
