export interface IMotifListeDepense {
  id?: number;
  libelle?: string | null;
  montant?: number | null;
}

export class MotifListeDepense implements IMotifListeDepense {
  constructor(public id?: number, public libelle?: string | null, public montant?: number | null) {}
}

export function getMotifListeDepenseIdentifier(motifListeDepense: IMotifListeDepense): number | undefined {
  return motifListeDepense.id;
}
