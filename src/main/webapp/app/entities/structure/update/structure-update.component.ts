import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStructure, Structure } from '../structure.model';
import { StructureService } from '../service/structure.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';
import { IPersonne, Personne } from '../../personne/personne.model';
import { createMask } from '@ngneat/input-mask';
import { PackService } from '../../pack/service/pack.service';
import { MatDialog } from '@angular/material/dialog';
import { NzModalService } from 'ng-zorro-antd/modal';
import { ProgressDialogComponent } from '../../../shared/progress-dialog/progress-dialog.component';
import { IPack } from '../../pack/pack.model';
import { User } from '../../../admin/user-management/user-management.model';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-structure-update',
  templateUrl: './structure-update.component.html',
  styleUrls: ['../structure.component.scss'],
})
export class StructureUpdateComponent implements OnInit {
  isSaving = false;
  typeStructureValues = Object.keys(TypeStructure);
  pack?: IPack;
  authorities: string[] = [];
  authoritiesSelected: string[] = [];
  user?: User;

  editForm = this.fb.group({
    id: [],
    denomination: [],
    ifu: [],
    rccm: [],
    codePostal: [],
    localisation: [],
    contact: [],
    regime: [],
    division: [],
    email: [],
    logo: [],
    logoContentType: [],
    cachet: [],
    cachetContentType: [],
    signature: [],
    signatureContentType: [],
    dateConfig: [],
    pdg: [],
    type: [],

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

    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    emailU: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
  });
  licence?: string;
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

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute,
    protected packService: PackService,
    private dialog: MatDialog,
    private modal: NzModalService,
    private modalService: NgbModal,
    private router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ structure }) => {
      if (structure.id === undefined) {
        structure.dateConfig = dayjs().startOf('day');
      }

      this.updateForm(structure);
    });
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const structure = this.createFromForm();
    if (structure.id !== undefined) {
      this.subscribeToSaveResponse(this.structureService.update(structure));
    } else {
      this.subscribeToSaveResponse(this.structureService.create(structure));
    }
  }

  saveConfig(): void {
    // this.isSaving = true;
    const structure = this.createFromForm();
    const dialogRef = this.modalService.open(ProgressDialogComponent,
      { backdrop: 'static', centered: true, windowClass: 'myCustomModalClass' });
    this.structureService.create(structure).subscribe(
      res => {
        if (res.body) {
          this.success('Configuration éffectuer avec succès!\n Veuillez vous connecter avec vos identifiants!');
          dialogRef.close();
          this.router.navigate(['login']);
        } else {
          this.warning('Une erreur est survenue');
          dialogRef.close();
          this.isSaving = false;
        }
      },
      () => {
        this.error('Veuillez verifiez votre connexion!');
        dialogRef.close();
        this.isSaving = false;
      }
    );
  }

  getLicence(): void {
    const dialogRef = this.modalService.open(ProgressDialogComponent,
      { backdrop: 'static', centered: true, windowClass: 'myCustomModalClass' });

    this.packService.findKeys(this.licence!).subscribe(
      res => {
        if (res.body) {
          this.pack = res.body;
          dialogRef.close();
        } else {
          this.warning('Une erreur est survenue');
          dialogRef.close();
        }
      },
      () => {
        this.error('Veuillez verifiez votre connexion!');
        dialogRef.close();
      }
    );
  }

  getTypeStruck($event: any): void {
    this.authoritiesSelected = [];
    if ($event === 'SIEGE') {
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes('ROLE_USER'.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes('STRUCTURE_ADMIN'.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
      }
    } else if ($event === 'AGENCE') {
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes('ROLE_USER'.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes('AGENCE_ADMIN'.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
      }
    } else if ($event === 'MAGASIN') {
      const roleUser = this.authorities.find(a => a.toString().toLowerCase().includes('ROLE_USER'.toLowerCase()));
      const role = this.authorities.find(a => a.toString().toLowerCase().includes('MAGASIN_ADMIN'.toLowerCase()));
      if (roleUser != null && role != null) {
        this.authoritiesSelected.push(roleUser);
        this.authoritiesSelected.push(role);
      }
    }
  }

  success(msg: string): void {
    this.modal.success({
      nzContent: msg,
      nzTitle: 'SUCCESS',
      nzOkText: 'OK',
    });
  }
  warning(msg: string): void {
    this.modal.warning({
      nzContent: msg,
      nzTitle: 'ATTENTION',
      nzOkText: 'OK',
    });
  }
  error(msg: string): void {
    this.modal.error({
      nzContent: msg,
      nzTitle: 'ERROR',
      nzOkText: 'OK',
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStructure>>): void {
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

  protected updateForm(structure: IStructure): void {
    this.editForm.patchValue({
      id: structure.id,
      denomination: structure.denomination,
      ifu: structure.ifu,
      rccm: structure.rccm,
      codePostal: structure.codePostal,
      localisation: structure.localisation,
      contact: structure.contact,
      regime: structure.regime,
      division: structure.division,
      email: structure.email,
      logo: structure.logo,
      logoContentType: structure.logoContentType,
      cachet: structure.cachet,
      cachetContentType: structure.cachetContentType,
      signature: structure.signature,
      signatureContentType: structure.signatureContentType,
      dateConfig: structure.dateConfig ? structure.dateConfig.format(DATE_TIME_FORMAT) : null,
      pdg: structure.pdg,
      type: structure.type,
    });
  }

  protected createFromForm(): IStructure {
    return {
      ...new Structure(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      ifu: this.editForm.get(['ifu'])!.value,
      rccm: this.editForm.get(['rccm'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      regime: this.editForm.get(['regime'])!.value,
      division: this.editForm.get(['division'])!.value,
      email: this.editForm.get(['email'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      cachetContentType: this.editForm.get(['cachetContentType'])!.value,
      cachet: this.editForm.get(['cachet'])!.value,
      signatureContentType: this.editForm.get(['signatureContentType'])!.value,
      signature: this.editForm.get(['signature'])!.value,
      type: this.editForm.get(['type'])!.value,
      personne: this.createFromFormPresonne(),
      userDTO: this.updateUser(),
      packDTO: this.pack,
    };
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

  private updateUser(): User {
    return {
      ...new User(),
      login: this.editForm.get(['login'])!.value,
      email: this.editForm.get(['email'])!.value,
      password: this.editForm.get(['password'])!.value,
      authorities: this.authoritiesSelected,
    };
  }
}
