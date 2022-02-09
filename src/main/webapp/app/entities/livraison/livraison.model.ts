import dayjs from 'dayjs/esm';
import { ICommande } from 'app/entities/commande/commande.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface ILivraison {
  id?: number;
  numero?: string | null;
  dateLivraison?: dayjs.Dayjs | null;
  commande?: ICommande | null;
  operateur?: IPersonne | null;
}

export class Livraison implements ILivraison {
  constructor(
    public id?: number,
    public numero?: string | null,
    public dateLivraison?: dayjs.Dayjs | null,
    public commande?: ICommande | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getLivraisonIdentifier(livraison: ILivraison): number | undefined {
  return livraison.id;
}
