import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MedicamentService } from '../service/medicament.service';
import { IMedicament, Medicament } from '../medicament.model';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { MedicamentUpdateComponent } from './medicament-update.component';

describe('Medicament Management Update Component', () => {
  let comp: MedicamentUpdateComponent;
  let fixture: ComponentFixture<MedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicamentService: MedicamentService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MedicamentUpdateComponent],
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
      .overrideTemplate(MedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicamentService = TestBed.inject(MedicamentService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Structure query and add missing value', () => {
      const medicament: IMedicament = { id: 456 };
      const structure: IStructure = { id: 43195 };
      medicament.structure = structure;

      const structureCollection: IStructure[] = [{ id: 39692 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [structure];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicament: IMedicament = { id: 456 };
      const structure: IStructure = { id: 37815 };
      medicament.structure = structure;

      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(medicament));
      expect(comp.structuresSharedCollection).toContain(structure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Medicament>>();
      const medicament = { id: 123 };
      jest.spyOn(medicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicament }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicamentService.update).toHaveBeenCalledWith(medicament);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Medicament>>();
      const medicament = new Medicament();
      jest.spyOn(medicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicament }));
      saveSubject.complete();

      // THEN
      expect(medicamentService.create).toHaveBeenCalledWith(medicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Medicament>>();
      const medicament = { id: 123 };
      jest.spyOn(medicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicamentService.update).toHaveBeenCalledWith(medicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStructureById', () => {
      it('Should return tracked Structure primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStructureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
