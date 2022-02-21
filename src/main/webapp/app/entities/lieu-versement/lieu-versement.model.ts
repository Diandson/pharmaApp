export interface ILieuVersement {
  id?: number;
  libelle?: string | null;
}

export class LieuVersement implements ILieuVersement {
  constructor(public id?: number, public libelle?: string | null) {}
}

export function getLieuVersementIdentifier(lieuVersement: ILieuVersement): number | undefined {
  return lieuVersement.id;
}
