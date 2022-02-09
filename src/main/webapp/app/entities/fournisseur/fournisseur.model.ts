import { IPersonne } from 'app/entities/personne/personne.model';

export interface IFournisseur {
  id?: number;
  libelle?: string | null;
  email?: string | null;
  operateur?: IPersonne | null;
}

export class Fournisseur implements IFournisseur {
  constructor(public id?: number, public libelle?: string | null, public email?: string | null, public operateur?: IPersonne | null) {}
}

export function getFournisseurIdentifier(fournisseur: IFournisseur): number | undefined {
  return fournisseur.id;
}
