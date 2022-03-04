import { NbMenuItem } from '@nebular/theme';

export const StructAdminMenu: NbMenuItem[] = [

  {
    title: 'PERSONNEL',
    pathMatch: 'full',
    link: '/personne',
  },
  {
    title: 'PARAMETRES',
    icon: 'settings',
    children: [
      {
        title: 'Les assurances',
        pathMatch: 'full',
        link: 'assurance'
      },
      {
        title: 'Les fournisseurs',
        pathMatch: 'full',
        link: 'fournisseur'
      },
      {
        title: 'Type de dépenses',
        pathMatch: 'full',
        link: 'motif-liste-depense'
      },
      {
        title: 'Lieu versement',
        pathMatch: 'full',
        link: 'lieu-versement'
      },
    ]
  },

  {
    title: 'LES OPERATIONS',
    icon: 'layers',
    children: [
      {
        title: 'Journal de vente',
        pathMatch: 'full',
        link: 'vente'
      },
      {
        title: 'Journal de dépense',
        pathMatch: 'full',
        link: 'depense'
      },
      {
        title: 'Journal de versement',
        pathMatch: 'full',
        link: 'versement'
      },
      {
        title: 'Les approvisionnements',
        pathMatch: 'full',
        link: 'approvisionnement'
      },
      {
        title: 'Les commandes',
        pathMatch: 'full',
        link: 'commande'
      },
      {
        title: 'Les livraisons',
        pathMatch: 'full',
        link: 'livraison'
      },
      {
        title: 'Lieu versement',
        pathMatch: 'full',
        link: 'lieu-versement'
      },
      {
        title: 'Les clients',
        pathMatch: 'full',
        link: 'client'
      },
    ]
  }
];
