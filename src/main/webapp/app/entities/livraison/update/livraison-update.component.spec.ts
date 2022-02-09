import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivraisonService } from '../service/livraison.service';
import { ILivraison, Livraison } from '../livraison.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { LivraisonUpdateComponent } from './livraison-update.component';

describe('Livraison Management Update Component', () => {
  let comp: LivraisonUpdateComponent;
  let fixture: ComponentFixture<LivraisonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livraisonService: LivraisonService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivraisonUpdateComponent],
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
      .overrideTemplate(LivraisonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivraisonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livraisonService = TestBed.inject(LivraisonService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const livraison: ILivraison = { id: 456 };
      const operateur: IPersonne = { id: 72974 };
      livraison.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 57516 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livraison });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const livraison: ILivraison = { id: 456 };
      const operateur: IPersonne = { id: 25904 };
      livraison.operateur = operateur;

      activatedRoute.data = of({ livraison });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(livraison));
      expect(comp.personnesSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livraison>>();
      const livraison = { id: 123 };
      jest.spyOn(livraisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livraison }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(livraisonService.update).toHaveBeenCalledWith(livraison);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livraison>>();
      const livraison = new Livraison();
      jest.spyOn(livraisonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livraison }));
      saveSubject.complete();

      // THEN
      expect(livraisonService.create).toHaveBeenCalledWith(livraison);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livraison>>();
      const livraison = { id: 123 };
      jest.spyOn(livraisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livraison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livraisonService.update).toHaveBeenCalledWith(livraison);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonneById', () => {
      it('Should return tracked Personne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
