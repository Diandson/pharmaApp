import dayjs from 'dayjs/esm';
import { IVersement } from 'app/entities/versement/versement.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { IVente } from 'app/entities/vente/vente.model';

export interface IPaiement {
  id?: number;
  numero?: string | null;
  numeroVente?: string | null;
  datePaiement?: dayjs.Dayjs | null;
  sommeRecu?: number | null;
  sommeDonner?: number | null;
  avoir?: number | null;
  versement?: IVersement | null;
  operateur?: IPersonne | null;
  vente?: IVente | null;
}

export class Paiement implements IPaiement {
  constructor(
    public id?: number,
    public numero?: string | null,
    public numeroVente?: string | null,
    public datePaiement?: dayjs.Dayjs | null,
    public sommeRecu?: number | null,
    public sommeDonner?: number | null,
    public avoir?: number | null,
    public versement?: IVersement | null,
    public operateur?: IPersonne | null,
    public vente?: IVente | null
  ) {}
}

export function getPaiementIdentifier(paiement: IPaiement): number | undefined {
  return paiement.id;
}
