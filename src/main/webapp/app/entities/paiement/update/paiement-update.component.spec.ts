import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaiementService } from '../service/paiement.service';
import { IPaiement, Paiement } from '../paiement.model';
import { IVersement } from 'app/entities/versement/versement.model';
import { VersementService } from 'app/entities/versement/service/versement.service';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

import { PaiementUpdateComponent } from './paiement-update.component';

describe('Paiement Management Update Component', () => {
  let comp: PaiementUpdateComponent;
  let fixture: ComponentFixture<PaiementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementService: PaiementService;
  let versementService: VersementService;
  let personneService: PersonneService;
  let venteService: VenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaiementUpdateComponent],
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
      .overrideTemplate(PaiementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementService = TestBed.inject(PaiementService);
    versementService = TestBed.inject(VersementService);
    personneService = TestBed.inject(PersonneService);
    venteService = TestBed.inject(VenteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Versement query and add missing value', () => {
      const paiement: IPaiement = { id: 456 };
      const versement: IVersement = { id: 60494 };
      paiement.versement = versement;

      const versementCollection: IVersement[] = [{ id: 45313 }];
      jest.spyOn(versementService, 'query').mockReturnValue(of(new HttpResponse({ body: versementCollection })));
      const additionalVersements = [versement];
      const expectedCollection: IVersement[] = [...additionalVersements, ...versementCollection];
      jest.spyOn(versementService, 'addVersementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(versementService.query).toHaveBeenCalled();
      expect(versementService.addVersementToCollectionIfMissing).toHaveBeenCalledWith(versementCollection, ...additionalVersements);
      expect(comp.versementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personne query and add missing value', () => {
      const paiement: IPaiement = { id: 456 };
      const operateur: IPersonne = { id: 39246 };
      paiement.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 77810 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call vente query and add missing value', () => {
      const paiement: IPaiement = { id: 456 };
      const vente: IVente = { id: 32504 };
      paiement.vente = vente;

      const venteCollection: IVente[] = [{ id: 38168 }];
      jest.spyOn(venteService, 'query').mockReturnValue(of(new HttpResponse({ body: venteCollection })));
      const expectedCollection: IVente[] = [vente, ...venteCollection];
      jest.spyOn(venteService, 'addVenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(venteService.query).toHaveBeenCalled();
      expect(venteService.addVenteToCollectionIfMissing).toHaveBeenCalledWith(venteCollection, vente);
      expect(comp.ventesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiement: IPaiement = { id: 456 };
      const versement: IVersement = { id: 79491 };
      paiement.versement = versement;
      const operateur: IPersonne = { id: 42859 };
      paiement.operateur = operateur;
      const vente: IVente = { id: 33160 };
      paiement.vente = vente;

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(paiement));
      expect(comp.versementsSharedCollection).toContain(versement);
      expect(comp.personnesSharedCollection).toContain(operateur);
      expect(comp.ventesCollection).toContain(vente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paiement>>();
      const paiement = { id: 123 };
      jest.spyOn(paiementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementService.update).toHaveBeenCalledWith(paiement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paiement>>();
      const paiement = new Paiement();
      jest.spyOn(paiementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiement }));
      saveSubject.complete();

      // THEN
      expect(paiementService.create).toHaveBeenCalledWith(paiement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Paiement>>();
      const paiement = { id: 123 };
      jest.spyOn(paiementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementService.update).toHaveBeenCalledWith(paiement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVersementById', () => {
      it('Should return tracked Versement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVersementById(0, entity);
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

    describe('trackVenteById', () => {
      it('Should return tracked Vente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVenteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
