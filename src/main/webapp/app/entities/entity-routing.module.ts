import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {GetstartedComponent} from "./getstarted/getstarted.component";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'structure',
        data: { pageTitle: 'Structure' },
        loadChildren: () => import('./structure/structure.module').then(m => m.StructureModule),
      },
      {
        path: 'getstarted',
        data: { pageTitle: 'Configuration' },
        component: GetstartedComponent
      },
      {
        path: 'medicament',
        data: { pageTitle: 'appPharmaApp.medicament.home.title' },
        loadChildren: () => import('./medicament/medicament.module').then(m => m.MedicamentModule),
      },
      {
        path: 'personne',
        data: { pageTitle: 'appPharmaApp.personne.home.title' },
        loadChildren: () => import('./personne/personne.module').then(m => m.PersonneModule),
      },
      {
        path: 'pack',
        data: { pageTitle: 'appPharmaApp.pack.home.title' },
        loadChildren: () => import('./pack/pack.module').then(m => m.PackModule),
      },
      {
        path: 'type-pack',
        data: { pageTitle: 'appPharmaApp.typePack.home.title' },
        loadChildren: () => import('./type-pack/type-pack.module').then(m => m.TypePackModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'appPharmaApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'commande-medicament',
        data: { pageTitle: 'appPharmaApp.commandeMedicament.home.title' },
        loadChildren: () => import('./commande-medicament/commande-medicament.module').then(m => m.CommandeMedicamentModule),
      },
      {
        path: 'assurance',
        data: { pageTitle: 'appPharmaApp.assurance.home.title' },
        loadChildren: () => import('./assurance/assurance.module').then(m => m.AssuranceModule),
      },
      {
        path: 'livraison',
        data: { pageTitle: 'appPharmaApp.livraison.home.title' },
        loadChildren: () => import('./livraison/livraison.module').then(m => m.LivraisonModule),
      },
      {
        path: 'vente',
        data: { pageTitle: 'appPharmaApp.vente.home.title' },
        loadChildren: () => import('./vente/vente.module').then(m => m.VenteModule),
      },
      {
        path: 'vente-medicament',
        data: { pageTitle: 'appPharmaApp.venteMedicament.home.title' },
        loadChildren: () => import('./vente-medicament/vente-medicament.module').then(m => m.VenteMedicamentModule),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'appPharmaApp.fournisseur.home.title' },
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.FournisseurModule),
      },
      {
        path: 'inventaire',
        data: { pageTitle: 'appPharmaApp.inventaire.home.title' },
        loadChildren: () => import('./inventaire/inventaire.module').then(m => m.InventaireModule),
      },
      {
        path: 'inventaire-medicament',
        data: { pageTitle: 'appPharmaApp.inventaireMedicament.home.title' },
        loadChildren: () => import('./inventaire-medicament/inventaire-medicament.module').then(m => m.InventaireMedicamentModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'appPharmaApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'appPharmaApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      {
        path: 'message',
        data: { pageTitle: 'appPharmaApp.message.home.title' },
        loadChildren: () => import('./message/message.module').then(m => m.MessageModule),
      },
      {
        path: 'approvisionnement',
        data: { pageTitle: 'appPharmaApp.approvisionnement.home.title' },
        loadChildren: () => import('./approvisionnement/approvisionnement.module').then(m => m.ApprovisionnementModule),
      },
      {
        path: 'approvisionnement-medicament',
        data: { pageTitle: 'appPharmaApp.approvisionnementMedicament.home.title' },
        loadChildren: () =>
          import('./approvisionnement-medicament/approvisionnement-medicament.module').then(m => m.ApprovisionnementMedicamentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
