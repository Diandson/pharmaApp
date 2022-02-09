import dayjs from 'dayjs/esm';
import { IApprovisionnementMedicament } from 'app/entities/approvisionnement-medicament/approvisionnement-medicament.model';

export interface IApprovisionnement {
  id?: number;
  numero?: string | null;
  agenceApp?: string | null;
  commentaire?: string | null;
  dateCommande?: dayjs.Dayjs | null;
  approvisionnementMedicaments?: IApprovisionnementMedicament[] | null;
}

export class Approvisionnement implements IApprovisionnement {
  constructor(
    public id?: number,
    public numero?: string | null,
    public agenceApp?: string | null,
    public commentaire?: string | null,
    public dateCommande?: dayjs.Dayjs | null,
    public approvisionnementMedicaments?: IApprovisionnementMedicament[] | null
  ) {}
}

export function getApprovisionnementIdentifier(approvisionnement: IApprovisionnement): number | undefined {
  return approvisionnement.id;
}
