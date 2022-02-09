import dayjs from 'dayjs/esm';
import { IStructure } from 'app/entities/structure/structure.model';
import { ITypePack } from 'app/entities/type-pack/type-pack.model';

export interface IPack {
  id?: number;
  libelle?: string | null;
  durer?: number | null;
  valide?: boolean | null;
  dateRenew?: dayjs.Dayjs | null;
  operateur?: IStructure | null;
  type?: ITypePack | null;
}

export class Pack implements IPack {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public durer?: number | null,
    public valide?: boolean | null,
    public dateRenew?: dayjs.Dayjs | null,
    public operateur?: IStructure | null,
    public type?: ITypePack | null
  ) {
    this.valide = this.valide ?? false;
  }
}

export function getPackIdentifier(pack: IPack): number | undefined {
  return pack.id;
}
