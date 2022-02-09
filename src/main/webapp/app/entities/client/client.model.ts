import { IAssurance } from 'app/entities/assurance/assurance.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IClient {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  telephone?: string | null;
  numeroAssure?: string | null;
  assurance?: IAssurance | null;
  operateur?: IPersonne | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public telephone?: string | null,
    public numeroAssure?: string | null,
    public assurance?: IAssurance | null,
    public operateur?: IPersonne | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
