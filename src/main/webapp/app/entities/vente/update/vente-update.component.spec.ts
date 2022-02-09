import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VenteService } from '../service/vente.service';
import { IVente, Vente } from '../vente.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { VenteUpdateComponent } from './vente-update.component';

describe('Vente Management Update Component', () => {
  let comp: VenteUpdateComponent;
  let fixture: ComponentFixture<VenteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let venteService: VenteService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VenteUpdateComponent],
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
      .overrideTemplate(VenteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VenteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    venteService = TestBed.inject(VenteService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const vente: IVente = { id: 456 };
      const operateur: IPersonne = { id: 13141 };
      vente.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 81467 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vente: IVente = { id: 456 };
      const operateur: IPersonne = { id: 89954 };
      vente.operateur = operateur;

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vente));
      expect(comp.personnesSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vente>>();
      const vente = { id: 123 };
      jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vente }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(venteService.update).toHaveBeenCalledWith(vente);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vente>>();
      const vente = new Vente();
      jest.spyOn(venteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vente }));
      saveSubject.complete();

      // THEN
      expect(venteService.create).toHaveBeenCalledWith(vente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vente>>();
      const vente = { id: 123 };
      jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(venteService.update).toHaveBeenCalledWith(vente);
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
