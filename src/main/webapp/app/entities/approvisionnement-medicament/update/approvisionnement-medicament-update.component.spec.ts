import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApprovisionnementMedicamentService } from '../service/approvisionnement-medicament.service';
import { IApprovisionnementMedicament, ApprovisionnementMedicament } from '../approvisionnement-medicament.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IApprovisionnement } from 'app/entities/approvisionnement/approvisionnement.model';
import { ApprovisionnementService } from 'app/entities/approvisionnement/service/approvisionnement.service';

import { ApprovisionnementMedicamentUpdateComponent } from './approvisionnement-medicament-update.component';

describe('ApprovisionnementMedicament Management Update Component', () => {
  let comp: ApprovisionnementMedicamentUpdateComponent;
  let fixture: ComponentFixture<ApprovisionnementMedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let approvisionnementMedicamentService: ApprovisionnementMedicamentService;
  let medicamentService: MedicamentService;
  let approvisionnementService: ApprovisionnementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApprovisionnementMedicamentUpdateComponent],
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
      .overrideTemplate(ApprovisionnementMedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApprovisionnementMedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    approvisionnementMedicamentService = TestBed.inject(ApprovisionnementMedicamentService);
    medicamentService = TestBed.inject(MedicamentService);
    approvisionnementService = TestBed.inject(ApprovisionnementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medicament query and add missing value', () => {
      const approvisionnementMedicament: IApprovisionnementMedicament = { id: 456 };
      const medicament: IMedicament = { id: 91999 };
      approvisionnementMedicament.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 46320 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(medicamentCollection, ...additionalMedicaments);
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Approvisionnement query and add missing value', () => {
      const approvisionnementMedicament: IApprovisionnementMedicament = { id: 456 };
      const approvionnement: IApprovisionnement = { id: 96995 };
      approvisionnementMedicament.approvionnement = approvionnement;

      const approvisionnementCollection: IApprovisionnement[] = [{ id: 2196 }];
      jest.spyOn(approvisionnementService, 'query').mockReturnValue(of(new HttpResponse({ body: approvisionnementCollection })));
      const additionalApprovisionnements = [approvionnement];
      const expectedCollection: IApprovisionnement[] = [...additionalApprovisionnements, ...approvisionnementCollection];
      jest.spyOn(approvisionnementService, 'addApprovisionnementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      expect(approvisionnementService.query).toHaveBeenCalled();
      expect(approvisionnementService.addApprovisionnementToCollectionIfMissing).toHaveBeenCalledWith(
        approvisionnementCollection,
        ...additionalApprovisionnements
      );
      expect(comp.approvisionnementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const approvisionnementMedicament: IApprovisionnementMedicament = { id: 456 };
      const medicament: IMedicament = { id: 73517 };
      approvisionnementMedicament.medicament = medicament;
      const approvionnement: IApprovisionnement = { id: 64604 };
      approvisionnementMedicament.approvionnement = approvionnement;

      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(approvisionnementMedicament));
      expect(comp.medicamentsSharedCollection).toContain(medicament);
      expect(comp.approvisionnementsSharedCollection).toContain(approvionnement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApprovisionnementMedicament>>();
      const approvisionnementMedicament = { id: 123 };
      jest.spyOn(approvisionnementMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvisionnementMedicament }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(approvisionnementMedicamentService.update).toHaveBeenCalledWith(approvisionnementMedicament);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApprovisionnementMedicament>>();
      const approvisionnementMedicament = new ApprovisionnementMedicament();
      jest.spyOn(approvisionnementMedicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvisionnementMedicament }));
      saveSubject.complete();

      // THEN
      expect(approvisionnementMedicamentService.create).toHaveBeenCalledWith(approvisionnementMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApprovisionnementMedicament>>();
      const approvisionnementMedicament = { id: 123 };
      jest.spyOn(approvisionnementMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnementMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(approvisionnementMedicamentService.update).toHaveBeenCalledWith(approvisionnementMedicament);
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

    describe('trackApprovisionnementById', () => {
      it('Should return tracked Approvisionnement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApprovisionnementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
