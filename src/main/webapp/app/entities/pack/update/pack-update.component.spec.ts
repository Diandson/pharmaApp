import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PackService } from '../service/pack.service';
import { IPack, Pack } from '../pack.model';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { ITypePack } from 'app/entities/type-pack/type-pack.model';
import { TypePackService } from 'app/entities/type-pack/service/type-pack.service';

import { PackUpdateComponent } from './pack-update.component';

describe('Pack Management Update Component', () => {
  let comp: PackUpdateComponent;
  let fixture: ComponentFixture<PackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let packService: PackService;
  let structureService: StructureService;
  let typePackService: TypePackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PackUpdateComponent],
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
      .overrideTemplate(PackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    packService = TestBed.inject(PackService);
    structureService = TestBed.inject(StructureService);
    typePackService = TestBed.inject(TypePackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Structure query and add missing value', () => {
      const pack: IPack = { id: 456 };
      const operateur: IStructure = { id: 94316 };
      pack.operateur = operateur;
      const structure: IStructure = { id: 93276 };
      pack.structure = structure;

      const structureCollection: IStructure[] = [{ id: 45498 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [operateur, structure];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(structureCollection, ...additionalStructures);
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypePack query and add missing value', () => {
      const pack: IPack = { id: 456 };
      const type: ITypePack = { id: 99505 };
      pack.type = type;

      const typePackCollection: ITypePack[] = [{ id: 84146 }];
      jest.spyOn(typePackService, 'query').mockReturnValue(of(new HttpResponse({ body: typePackCollection })));
      const additionalTypePacks = [type];
      const expectedCollection: ITypePack[] = [...additionalTypePacks, ...typePackCollection];
      jest.spyOn(typePackService, 'addTypePackToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      expect(typePackService.query).toHaveBeenCalled();
      expect(typePackService.addTypePackToCollectionIfMissing).toHaveBeenCalledWith(typePackCollection, ...additionalTypePacks);
      expect(comp.typePacksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pack: IPack = { id: 456 };
      const operateur: IStructure = { id: 56164 };
      pack.operateur = operateur;
      const structure: IStructure = { id: 34142 };
      pack.structure = structure;
      const type: ITypePack = { id: 86648 };
      pack.type = type;

      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pack));
      expect(comp.structuresSharedCollection).toContain(operateur);
      expect(comp.structuresSharedCollection).toContain(structure);
      expect(comp.typePacksSharedCollection).toContain(type);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pack>>();
      const pack = { id: 123 };
      jest.spyOn(packService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pack }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(packService.update).toHaveBeenCalledWith(pack);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pack>>();
      const pack = new Pack();
      jest.spyOn(packService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pack }));
      saveSubject.complete();

      // THEN
      expect(packService.create).toHaveBeenCalledWith(pack);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pack>>();
      const pack = { id: 123 };
      jest.spyOn(packService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(packService.update).toHaveBeenCalledWith(pack);
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

    describe('trackTypePackById', () => {
      it('Should return tracked TypePack primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypePackById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
