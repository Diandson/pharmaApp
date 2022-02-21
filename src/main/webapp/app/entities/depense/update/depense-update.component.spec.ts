import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepenseService } from '../service/depense.service';
import { IDepense, Depense } from '../depense.model';

import { DepenseUpdateComponent } from './depense-update.component';

describe('Depense Management Update Component', () => {
  let comp: DepenseUpdateComponent;
  let fixture: ComponentFixture<DepenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depenseService: DepenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepenseUpdateComponent],
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
      .overrideTemplate(DepenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depenseService = TestBed.inject(DepenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const depense: IDepense = { id: 456 };

      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(depense));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Depense>>();
      const depense = { id: 123 };
      jest.spyOn(depenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depense }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depenseService.update).toHaveBeenCalledWith(depense);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Depense>>();
      const depense = new Depense();
      jest.spyOn(depenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depense }));
      saveSubject.complete();

      // THEN
      expect(depenseService.create).toHaveBeenCalledWith(depense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Depense>>();
      const depense = { id: 123 };
      jest.spyOn(depenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depenseService.update).toHaveBeenCalledWith(depense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
