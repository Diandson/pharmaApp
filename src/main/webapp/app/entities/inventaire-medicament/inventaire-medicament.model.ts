import dayjs from 'dayjs/esm';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IInventaire } from 'app/entities/inventaire/inventaire.model';

export interface IInventaireMedicament {
  id?: number;
  stockTheorique?: number | null;
  stockPhysique?: number | null;
  stockDifferent?: number | null;
  dateFabrication?: dayjs.Dayjs | null;
  dateExpiration?: dayjs.Dayjs | null;
  medicament?: IMedicament | null;
  inventaire?: IInventaire | null;
}

export class InventaireMedicament implements IInventaireMedicament {
  constructor(
    public id?: number,
    public stockTheorique?: number | null,
    public stockPhysique?: number | null,
    public stockDifferent?: number | null,
    public dateFabrication?: dayjs.Dayjs | null,
    public dateExpiration?: dayjs.Dayjs | null,
    public medicament?: IMedicament | null,
    public inventaire?: IInventaire | null
  ) {}
}

export function getInventaireMedicamentIdentifier(inventaireMedicament: IInventaireMedicament): number | undefined {
  return inventaireMedicament.id;
}
