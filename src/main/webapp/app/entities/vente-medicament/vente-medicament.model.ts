import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IVente } from 'app/entities/vente/vente.model';

export interface IVenteMedicament {
  id?: number;
  quantite?: number | null;
  montant?: string | null;
  medicament?: IMedicament | null;
  vente?: IVente | null;
}

export class VenteMedicament implements IVenteMedicament {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public montant?: string | null,
    public medicament?: IMedicament | null,
    public vente?: IVente | null
  ) {}
}

export function getVenteMedicamentIdentifier(venteMedicament: IVenteMedicament): number | undefined {
  return venteMedicament.id;
}
