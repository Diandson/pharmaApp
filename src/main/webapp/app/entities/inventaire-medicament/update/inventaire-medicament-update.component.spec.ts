import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InventaireMedicamentService } from '../service/inventaire-medicament.service';
import { IInventaireMedicament, InventaireMedicament } from '../inventaire-medicament.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IInventaire } from 'app/entities/inventaire/inventaire.model';
import { InventaireService } from 'app/entities/inventaire/service/inventaire.service';

import { InventaireMedicamentUpdateComponent } from './inventaire-medicament-update.component';

describe('InventaireMedicament Management Update Component', () => {
  let comp: InventaireMedicamentUpdateComponent;
  let fixture: ComponentFixture<InventaireMedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventaireMedicamentService: InventaireMedicamentService;
  let medicamentService: MedicamentService;
  let inventaireService: InventaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InventaireMedicamentUpdateComponent],
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
      .overrideTemplate(InventaireMedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventaireMedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventaireMedicamentService = TestBed.inject(InventaireMedicamentService);
    medicamentService = TestBed.inject(MedicamentService);
    inventaireService = TestBed.inject(InventaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medicament query and add missing value', () => {
      const inventaireMedicament: IInventaireMedicament = { id: 456 };
      const medicament: IMedicament = { id: 56116 };
      inventaireMedicament.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 3881 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(medicamentCollection, ...additionalMedicaments);
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inventaire query and add missing value', () => {
      const inventaireMedicament: IInventaireMedicament = { id: 456 };
      const inventaire: IInventaire = { id: 4770 };
      inventaireMedicament.inventaire = inventaire;

      const inventaireCollection: IInventaire[] = [{ id: 72703 }];
      jest.spyOn(inventaireService, 'query').mockReturnValue(of(new HttpResponse({ body: inventaireCollection })));
      const additionalInventaires = [inventaire];
      const expectedCollection: IInventaire[] = [...additionalInventaires, ...inventaireCollection];
      jest.spyOn(inventaireService, 'addInventaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      expect(inventaireService.query).toHaveBeenCalled();
      expect(inventaireService.addInventaireToCollectionIfMissing).toHaveBeenCalledWith(inventaireCollection, ...additionalInventaires);
      expect(comp.inventairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inventaireMedicament: IInventaireMedicament = { id: 456 };
      const medicament: IMedicament = { id: 1838 };
      inventaireMedicament.medicament = medicament;
      const inventaire: IInventaire = { id: 75037 };
      inventaireMedicament.inventaire = inventaire;

      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inventaireMedicament));
      expect(comp.medicamentsSharedCollection).toContain(medicament);
      expect(comp.inventairesSharedCollection).toContain(inventaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventaireMedicament>>();
      const inventaireMedicament = { id: 123 };
      jest.spyOn(inventaireMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventaireMedicament }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventaireMedicamentService.update).toHaveBeenCalledWith(inventaireMedicament);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventaireMedicament>>();
      const inventaireMedicament = new InventaireMedicament();
      jest.spyOn(inventaireMedicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventaireMedicament }));
      saveSubject.complete();

      // THEN
      expect(inventaireMedicamentService.create).toHaveBeenCalledWith(inventaireMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventaireMedicament>>();
      const inventaireMedicament = { id: 123 };
      jest.spyOn(inventaireMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaireMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventaireMedicamentService.update).toHaveBeenCalledWith(inventaireMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMedicamentById', () => {
      it('Should return tracked Medicament primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMedicamentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInventaireById', () => {
      it('Should return tracked Inventaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInventaireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
