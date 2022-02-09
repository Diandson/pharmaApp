import { IPack } from 'app/entities/pack/pack.model';

export interface ITypePack {
  id?: number;
  libelle?: string | null;
  durer?: number | null;
  prix?: number | null;
  annexe?: number | null;
  packs?: IPack[] | null;
}

export class TypePack implements ITypePack {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public durer?: number | null,
    public prix?: number | null,
    public annexe?: number | null,
    public packs?: IPack[] | null
  ) {}
}

export function getTypePackIdentifier(typePack: ITypePack): number | undefined {
  return typePack.id;
}
