import { IPaiement } from 'app/entities/paiement/paiement.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IVersement {
  id?: number;
  numero?: string | null;
  commentaire?: string | null;
  montant?: number | null;
  resteAVerser?: number | null;
  lieuVersement?: string | null;
  referenceVersement?: string | null;
  identiteReceveur?: string | null;
  paiements?: IPaiement[] | null;
  operateur?: IPersonne | null;
}

export class Versement implements IVersement {
  constructor(
    public id?: number,
    public numero?: string | null,
    public commentaire?: string | null,
    public montant?: number | null,
    public resteAVerser?: number | null,
    public lieuVersement?: string | null,
    public referenceVersement?: string | null,
    public identiteReceveur?: string | null,
    public paiements?: IPaiement[] | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getVersementIdentifier(versement: IVersement): number | undefined {
  return versement.id;
}
