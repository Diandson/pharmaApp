import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MotifListeDepenseService } from '../service/motif-liste-depense.service';
import { IMotifListeDepense, MotifListeDepense } from '../motif-liste-depense.model';

import { MotifListeDepenseUpdateComponent } from './motif-liste-depense-update.component';

describe('MotifListeDepense Management Update Component', () => {
  let comp: MotifListeDepenseUpdateComponent;
  let fixture: ComponentFixture<MotifListeDepenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let motifListeDepenseService: MotifListeDepenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MotifListeDepenseUpdateComponent],
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
      .overrideTemplate(MotifListeDepenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MotifListeDepenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    motifListeDepenseService = TestBed.inject(MotifListeDepenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const motifListeDepense: IMotifListeDepense = { id: 456 };

      activatedRoute.data = of({ motifListeDepense });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(motifListeDepense));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifListeDepense>>();
      const motifListeDepense = { id: 123 };
      jest.spyOn(motifListeDepenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifListeDepense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifListeDepense }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(motifListeDepenseService.update).toHaveBeenCalledWith(motifListeDepense);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifListeDepense>>();
      const motifListeDepense = new MotifListeDepense();
      jest.spyOn(motifListeDepenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifListeDepense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifListeDepense }));
      saveSubject.complete();

      // THEN
      expect(motifListeDepenseService.create).toHaveBeenCalledWith(motifListeDepense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MotifListeDepense>>();
      const motifListeDepense = { id: 123 };
      jest.spyOn(motifListeDepenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifListeDepense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(motifListeDepenseService.update).toHaveBeenCalledWith(motifListeDepense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
