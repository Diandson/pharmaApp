import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IApprovisionnement } from 'app/entities/approvisionnement/approvisionnement.model';

export interface IApprovisionnementMedicament {
  id?: number;
  quantite?: number | null;
  medicament?: IMedicament | null;
  approvionnement?: IApprovisionnement | null;
}

export class ApprovisionnementMedicament implements IApprovisionnementMedicament {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public medicament?: IMedicament | null,
    public approvionnement?: IApprovisionnement | null
  ) {}
}

export function getApprovisionnementMedicamentIdentifier(approvisionnementMedicament: IApprovisionnementMedicament): number | undefined {
  return approvisionnementMedicament.id;
}
