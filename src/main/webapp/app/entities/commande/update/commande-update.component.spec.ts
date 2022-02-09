import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandeService } from '../service/commande.service';
import { ICommande, Commande } from '../commande.model';
import { ILivraison } from 'app/entities/livraison/livraison.model';
import { LivraisonService } from 'app/entities/livraison/service/livraison.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Commande Management Update Component', () => {
  let comp: CommandeUpdateComponent;
  let fixture: ComponentFixture<CommandeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandeService: CommandeService;
  let livraisonService: LivraisonService;
  let fournisseurService: FournisseurService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommandeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommandeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandeService = TestBed.inject(CommandeService);
    livraisonService = TestBed.inject(LivraisonService);
    fournisseurService = TestBed.inject(FournisseurService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call livraison query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const livraison: ILivraison = { id: 27456 };
      commande.livraison = livraison;

      const livraisonCollection: ILivraison[] = [{ id: 74269 }];
      jest.spyOn(livraisonService, 'query').mockReturnValue(of(new HttpResponse({ body: livraisonCollection })));
      const expectedCollection: ILivraison[] = [livraison, ...livraisonCollection];
      jest.spyOn(livraisonService, 'addLivraisonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(livraisonService.query).toHaveBeenCalled();
      expect(livraisonService.addLivraisonToCollectionIfMissing).toHaveBeenCalledWith(livraisonCollection, livraison);
      expect(comp.livraisonsCollection).toEqual(expectedCollection);
    });

    it('Should call fournisseur query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const fournisseur: IFournisseur = { id: 39900 };
      commande.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 64129 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const expectedCollection: IFournisseur[] = [fournisseur, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(fournisseurCollection, fournisseur);
      expect(comp.fournisseursCollection).toEqual(expectedCollection);
    });

    it('Should call Personne query and add missing value', () => {
      const commande: ICommande = { id: 456 };
      const operateur: IPersonne = { id: 61238 };
      commande.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 58048 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commande: ICommande = { id: 456 };
      const livraison: ILivraison = { id: 76956 };
      commande.livraison = livraison;
      const fournisseur: IFournisseur = { id: 28213 };
      commande.fournisseur = fournisseur;
      const operateur: IPersonne = { id: 87793 };
      commande.operateur = operateur;

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commande));
      expect(comp.livraisonsCollection).toContain(livraison);
      expect(comp.fournisseursCollection).toContain(fournisseur);
      expect(comp.personnesSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = { id: 123 };
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandeService.update).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = new Commande();
      jest.spyOn(commandeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(commandeService.create).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = { id: 123 };
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandeService.update).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLivraisonById', () => {
      it('Should return tracked Livraison primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLivraisonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFournisseurById', () => {
      it('Should return tracked Fournisseur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFournisseurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPersonneById', () => {
      it('Should return tracked Personne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
