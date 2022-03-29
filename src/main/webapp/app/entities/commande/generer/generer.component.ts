import { Component, OnInit } from '@angular/core';
import {CommandeService} from "../service/commande.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MedicamentService} from "../../medicament/service/medicament.service";
import {NzModalService} from "ng-zorro-antd/modal";
import {IMedicament} from "../../medicament/medicament.model";
import {createMask} from "@ngneat/input-mask";
import {Fournisseur, IFournisseur} from "../../fournisseur/fournisseur.model";
import {FournisseurService} from "../../fournisseur/service/fournisseur.service";

@Component({
  selector: 'jhi-generer',
  templateUrl: './generer.component.html',
  styleUrls: ['./generer.component.scss']
})
export class GenererComponent implements OnInit {
  medicaments: IMedicament[] = [];
  medicamentList: IMedicament[] = [];
  search?: string;
  prixTotal = 0;
  fournisseurs?: IFournisseur[];
  selectedFournisseur: IFournisseur = new Fournisseur();
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
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected medicamentService: MedicamentService,
    private modalService: NgbModal,
    private modal: NzModalService,
    protected fournisseurService: FournisseurService,
    protected activeModal: NgbActiveModal
  ) { }

  ngOnInit(): void {
    this.medicamentService.queryGenerer().subscribe(res => this.medicaments = res.body ?? []);
    this.medicaments.forEach(value => (this.prixTotal = this.prixTotal + (value.prixPublic! * value.stockTheorique!)));

    this.fournisseurService.query()
      .subscribe(res => this.fournisseurs = res.body ?? []);
  }

  removeMedicament(medi: IMedicament): void {
    this.modal.warning({
      nzContent: 'Voulez-vous vraiment retirer ce produit?',
      nzTitle: 'ATTENTION',
      nzOkText: 'Oui',
      nzCancelText: 'Non',
      nzOnOk: () => {
        this.prixTotal = 0;
        const index = this.medicaments.findIndex(value => value.id === medi.id);
        this.medicaments.splice(index, 1);
        this.medicaments.forEach(value => (this.prixTotal = this.prixTotal + value.prixPublic! * value.stockTheorique!));
      },
    });
  }

  getPrixAchat(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }
  getPrixPublic(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
  }
  getStockTheorique(): void {
    this.prixTotal = 0;
    this.medicamentList.forEach(value => (this.prixTotal = this.prixTotal + value.stockTheorique! * value.prixPublic!));
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

  close(): void {
    this.activeModal.close('close');
  }
}
