import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssuranceService } from '../service/assurance.service';
import { IAssurance, Assurance } from '../assurance.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { AssuranceUpdateComponent } from './assurance-update.component';

describe('Assurance Management Update Component', () => {
  let comp: AssuranceUpdateComponent;
  let fixture: ComponentFixture<AssuranceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assuranceService: AssuranceService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssuranceUpdateComponent],
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
      .overrideTemplate(AssuranceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssuranceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assuranceService = TestBed.inject(AssuranceService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const assurance: IAssurance = { id: 456 };
      const operateur: IPersonne = { id: 16477 };
      assurance.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 5949 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assurance: IAssurance = { id: 456 };
      const operateur: IPersonne = { id: 24863 };
      assurance.operateur = operateur;

      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assurance));
      expect(comp.personnesSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assurance>>();
      const assurance = { id: 123 };
      jest.spyOn(assuranceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assurance }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assuranceService.update).toHaveBeenCalledWith(assurance);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assurance>>();
      const assurance = new Assurance();
      jest.spyOn(assuranceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assurance }));
      saveSubject.complete();

      // THEN
      expect(assuranceService.create).toHaveBeenCalledWith(assurance);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Assurance>>();
      const assurance = { id: 123 };
      jest.spyOn(assuranceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assuranceService.update).toHaveBeenCalledWith(assurance);
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
