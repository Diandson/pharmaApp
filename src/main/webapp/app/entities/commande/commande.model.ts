import dayjs from 'dayjs/esm';
import { ILivraison } from 'app/entities/livraison/livraison.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { ICommandeMedicament } from 'app/entities/commande-medicament/commande-medicament.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import {IMedicament} from "../medicament/medicament.model";

export interface ICommande {
  id?: number;
  numero?: string | null;
  dateCommande?: dayjs.Dayjs | null;
  valider?: boolean | null;
  livraison?: ILivraison | null;
  fournisseur?: IFournisseur | null;
  commandeMedicaments?: ICommandeMedicament[] | null;
  medicament?: IMedicament[] | null;
  operateur?: IPersonne | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public numero?: string | null,
    public dateCommande?: dayjs.Dayjs | null,
    public valider?: boolean | null,
    public livraison?: ILivraison | null,
    public fournisseur?: IFournisseur | null,
    public commandeMedicaments?: ICommandeMedicament[] | null,
    public medicament?: IMedicament[] | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
