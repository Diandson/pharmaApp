import dayjs from 'dayjs/esm';
import { IStructure } from 'app/entities/structure/structure.model';

export interface IMedicament {
  id?: number;
  denomination?: string | null;
  dci?: string | null;
  forme?: string | null;
  dosage?: string | null;
  classe?: string | null;
  codeBare?: string | null;
  prixAchat?: number | null;
  prixPublic?: number | null;
  stockAlerte?: number | null;
  stockSecurite?: number | null;
  stockTheorique?: number | null;
  dateFabrication?: dayjs.Dayjs | null;
  dateExpiration?: dayjs.Dayjs | null;
  imageContentType?: string | null;
  image?: string | null;
  structure?: IStructure | null;
}

export class Medicament implements IMedicament {
  constructor(
    public id?: number,
    public denomination?: string | null,
    public dci?: string | null,
    public forme?: string | null,
    public dosage?: string | null,
    public classe?: string | null,
    public codeBare?: string | null,
    public prixAchat?: number | null,
    public prixPublic?: number | null,
    public stockAlerte?: number | null,
    public stockSecurite?: number | null,
    public stockTheorique?: number | null,
    public dateFabrication?: dayjs.Dayjs | null,
    public dateExpiration?: dayjs.Dayjs | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public structure?: IStructure | null
  ) {}
}

export function getMedicamentIdentifier(medicament: IMedicament): number | undefined {
  return medicament.id;
}
