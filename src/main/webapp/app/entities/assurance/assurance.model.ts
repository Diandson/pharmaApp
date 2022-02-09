import { IPersonne } from 'app/entities/personne/personne.model';

export interface IAssurance {
  id?: number;
  libelle?: string;
  taux?: number | null;
  email?: string | null;
  operateur?: IPersonne | null;
}

export class Assurance implements IAssurance {
  constructor(
    public id?: number,
    public libelle?: string,
    public taux?: number | null,
    public email?: string | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getAssuranceIdentifier(assurance: IAssurance): number | undefined {
  return assurance.id;
}
