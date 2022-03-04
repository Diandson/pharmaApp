import { IVenteMedicament } from 'app/entities/vente-medicament/vente-medicament.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { IMedicament } from '../medicament/medicament.model';

export interface IVente {
  id?: number;
  numero?: string | null;
  dateVente?: any | null;
  montant?: number | null;
  montantAssurance?: number | null;
  sommeDonne?: number | null;
  sommeRendu?: number | null;
  avoir?: number | null;
  venteMedicaments?: IVenteMedicament[] | null;
  operateur?: IPersonne | null;
  medicament?: IMedicament[] | null;
}

export class Vente implements IVente {
  constructor(
    public id?: number,
    public numero?: string | null,
    public dateVente?: any | null,
    public montant?: number | null,
    public montantAssurance?: number | null,
    public sommeDonne?: number | null,
    public sommeRendu?: number | null,
    public avoir?: number | null,
    public venteMedicaments?: IVenteMedicament[] | null,
    public operateur?: IPersonne | null,
    public medicament?: IMedicament[] | null
  ) {}
}

export function getVenteIdentifier(vente: IVente): number | undefined {
  return vente.id;
}
