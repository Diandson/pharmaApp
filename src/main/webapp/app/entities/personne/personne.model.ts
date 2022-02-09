import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { IAssurance } from 'app/entities/assurance/assurance.model';
import { IVente } from 'app/entities/vente/vente.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { IClient } from 'app/entities/client/client.model';
import { ILivraison } from 'app/entities/livraison/livraison.model';
import { IInventaire } from 'app/entities/inventaire/inventaire.model';
import { IMessage } from 'app/entities/message/message.model';
import { INotification } from 'app/entities/notification/notification.model';
import { IStructure } from 'app/entities/structure/structure.model';

export interface IPersonne {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  dataNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: string | null;
  numeroDoc?: string | null;
  profilContentType?: string | null;
  profil?: string | null;
  telephone?: string | null;
  user?: IUser | null;
  commandes?: ICommande[] | null;
  assurances?: IAssurance[] | null;
  ventes?: IVente[] | null;
  fournisseurs?: IFournisseur[] | null;
  clients?: IClient[] | null;
  livraisons?: ILivraison[] | null;
  inventaires?: IInventaire[] | null;
  messages?: IMessage[] | null;
  notifications?: INotification[] | null;
  structure?: IStructure | null;
}

export class Personne implements IPersonne {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public dataNaissance?: dayjs.Dayjs | null,
    public lieuNaissance?: string | null,
    public numeroDoc?: string | null,
    public profilContentType?: string | null,
    public profil?: string | null,
    public telephone?: string | null,
    public user?: IUser | null,
    public commandes?: ICommande[] | null,
    public assurances?: IAssurance[] | null,
    public ventes?: IVente[] | null,
    public fournisseurs?: IFournisseur[] | null,
    public clients?: IClient[] | null,
    public livraisons?: ILivraison[] | null,
    public inventaires?: IInventaire[] | null,
    public messages?: IMessage[] | null,
    public notifications?: INotification[] | null,
    public structure?: IStructure | null
  ) {}
}

export function getPersonneIdentifier(personne: IPersonne): number | undefined {
  return personne.id;
}
