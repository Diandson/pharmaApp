import { IInventaireMedicament } from 'app/entities/inventaire-medicament/inventaire-medicament.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IInventaire {
  id?: number;
  numero?: string | null;
  dateInventaire?: string | null;
  inventaireMedicaments?: IInventaireMedicament[] | null;
  operateur?: IPersonne | null;
}

export class Inventaire implements IInventaire {
  constructor(
    public id?: number,
    public numero?: string | null,
    public dateInventaire?: string | null,
    public inventaireMedicaments?: IInventaireMedicament[] | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getInventaireIdentifier(inventaire: IInventaire): number | undefined {
  return inventaire.id;
}
