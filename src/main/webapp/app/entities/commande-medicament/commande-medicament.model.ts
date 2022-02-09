import { IMedicament } from 'app/entities/medicament/medicament.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface ICommandeMedicament {
  id?: number;
  quantite?: number | null;
  medicament?: IMedicament | null;
  commande?: ICommande | null;
}

export class CommandeMedicament implements ICommandeMedicament {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public medicament?: IMedicament | null,
    public commande?: ICommande | null
  ) {}
}

export function getCommandeMedicamentIdentifier(commandeMedicament: ICommandeMedicament): number | undefined {
  return commandeMedicament.id;
}
