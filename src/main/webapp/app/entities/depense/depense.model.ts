import dayjs from 'dayjs/esm';

export interface IDepense {
  id?: number;
  numero?: string | null;
  motifDepense?: string | null;
  ordonnateur?: string | null;
  justificatif?: string | null;
  montant?: number | null;
  dateDepense?: dayjs.Dayjs | null;
}

export class Depense implements IDepense {
  constructor(
    public id?: number,
    public numero?: string | null,
    public motifDepense?: string | null,
    public ordonnateur?: string | null,
    public justificatif?: string | null,
    public montant?: number | null,
    public dateDepense?: dayjs.Dayjs | null
  ) {}
}

export function getDepenseIdentifier(depense: IDepense): number | undefined {
  return depense.id;
}
