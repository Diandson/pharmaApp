import dayjs from 'dayjs/esm';
import { IPack } from 'app/entities/pack/pack.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';

export interface IStructure {
  id?: number;
  denomination?: string | null;
  ifu?: string | null;
  rccm?: string | null;
  codePostal?: string | null;
  localisation?: string | null;
  contact?: string | null;
  regime?: string | null;
  division?: string | null;
  email?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  cachetContentType?: string | null;
  cachet?: string | null;
  signatureContentType?: string | null;
  signature?: string | null;
  dateConfig?: dayjs.Dayjs | null;
  pdg?: string | null;
  type?: TypeStructure | null;
  packs?: IPack[] | null;
  medicaments?: IMedicament[] | null;
  personnes?: IPersonne[] | null;
}

export class Structure implements IStructure {
  constructor(
    public id?: number,
    public denomination?: string | null,
    public ifu?: string | null,
    public rccm?: string | null,
    public codePostal?: string | null,
    public localisation?: string | null,
    public contact?: string | null,
    public regime?: string | null,
    public division?: string | null,
    public email?: string | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public cachetContentType?: string | null,
    public cachet?: string | null,
    public signatureContentType?: string | null,
    public signature?: string | null,
    public dateConfig?: dayjs.Dayjs | null,
    public pdg?: string | null,
    public type?: TypeStructure | null,
    public packs?: IPack[] | null,
    public medicaments?: IMedicament[] | null,
    public personnes?: IPersonne[] | null
  ) {}
}

export function getStructureIdentifier(structure: IStructure): number | undefined {
  return structure.id;
}
