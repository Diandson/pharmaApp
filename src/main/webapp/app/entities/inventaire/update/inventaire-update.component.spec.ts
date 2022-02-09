import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InventaireService } from '../service/inventaire.service';
import { IInventaire, Inventaire } from '../inventaire.model';
import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { InventaireUpdateComponent } from './inventaire-update.component';

describe('Inventaire Management Update Component', () => {
  let comp: InventaireUpdateComponent;
  let fixture: ComponentFixture<InventaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventaireService: InventaireService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InventaireUpdateComponent],
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
      .overrideTemplate(InventaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventaireService = TestBed.inject(InventaireService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const inventaire: IInventaire = { id: 456 };
      const operateur: IPersonne = { id: 49883 };
      inventaire.operateur = operateur;

      const personneCollection: IPersonne[] = [{ id: 3779 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [operateur];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(personneCollection, ...additionalPersonnes);
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inventaire: IInventaire = { id: 456 };
      const operateur: IPersonne = { id: 64076 };
      inventaire.operateur = operateur;

      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inventaire));
      expect(comp.personnesSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventaire>>();
      const inventaire = { id: 123 };
      jest.spyOn(inventaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventaireService.update).toHaveBeenCalledWith(inventaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventaire>>();
      const inventaire = new Inventaire();
      jest.spyOn(inventaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventaire }));
      saveSubject.complete();

      // THEN
      expect(inventaireService.create).toHaveBeenCalledWith(inventaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventaire>>();
      const inventaire = { id: 123 };
      jest.spyOn(inventaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventaireService.update).toHaveBeenCalledWith(inventaire);
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
