import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VenteMedicamentService } from '../service/vente-medicament.service';
import { IVenteMedicament, VenteMedicament } from '../vente-medicament.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IVente } from 'app/entities/vente/vente.model';
import { VenteService } from 'app/entities/vente/service/vente.service';

import { VenteMedicamentUpdateComponent } from './vente-medicament-update.component';

describe('VenteMedicament Management Update Component', () => {
  let comp: VenteMedicamentUpdateComponent;
  let fixture: ComponentFixture<VenteMedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let venteMedicamentService: VenteMedicamentService;
  let medicamentService: MedicamentService;
  let venteService: VenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VenteMedicamentUpdateComponent],
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
      .overrideTemplate(VenteMedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VenteMedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    venteMedicamentService = TestBed.inject(VenteMedicamentService);
    medicamentService = TestBed.inject(MedicamentService);
    venteService = TestBed.inject(VenteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medicament query and add missing value', () => {
      const venteMedicament: IVenteMedicament = { id: 456 };
      const medicament: IMedicament = { id: 8979 };
      venteMedicament.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 36745 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(medicamentCollection, ...additionalMedicaments);
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vente query and add missing value', () => {
      const venteMedicament: IVenteMedicament = { id: 456 };
      const vente: IVente = { id: 82554 };
      venteMedicament.vente = vente;

      const venteCollection: IVente[] = [{ id: 17549 }];
      jest.spyOn(venteService, 'query').mockReturnValue(of(new HttpResponse({ body: venteCollection })));
      const additionalVentes = [vente];
      const expectedCollection: IVente[] = [...additionalVentes, ...venteCollection];
      jest.spyOn(venteService, 'addVenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      expect(venteService.query).toHaveBeenCalled();
      expect(venteService.addVenteToCollectionIfMissing).toHaveBeenCalledWith(venteCollection, ...additionalVentes);
      expect(comp.ventesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const venteMedicament: IVenteMedicament = { id: 456 };
      const medicament: IMedicament = { id: 34728 };
      venteMedicament.medicament = medicament;
      const vente: IVente = { id: 65358 };
      venteMedicament.vente = vente;

      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(venteMedicament));
      expect(comp.medicamentsSharedCollection).toContain(medicament);
      expect(comp.ventesSharedCollection).toContain(vente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VenteMedicament>>();
      const venteMedicament = { id: 123 };
      jest.spyOn(venteMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venteMedicament }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(venteMedicamentService.update).toHaveBeenCalledWith(venteMedicament);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VenteMedicament>>();
      const venteMedicament = new VenteMedicament();
      jest.spyOn(venteMedicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venteMedicament }));
      saveSubject.complete();

      // THEN
      expect(venteMedicamentService.create).toHaveBeenCalledWith(venteMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VenteMedicament>>();
      const venteMedicament = { id: 123 };
      jest.spyOn(venteMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venteMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(venteMedicamentService.update).toHaveBeenCalledWith(venteMedicament);
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

    describe('trackVenteById', () => {
      it('Should return tracked Vente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVenteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
