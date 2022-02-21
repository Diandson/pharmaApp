import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LieuVersementService } from '../service/lieu-versement.service';
import { ILieuVersement, LieuVersement } from '../lieu-versement.model';

import { LieuVersementUpdateComponent } from './lieu-versement-update.component';

describe('LieuVersement Management Update Component', () => {
  let comp: LieuVersementUpdateComponent;
  let fixture: ComponentFixture<LieuVersementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lieuVersementService: LieuVersementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LieuVersementUpdateComponent],
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
      .overrideTemplate(LieuVersementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LieuVersementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lieuVersementService = TestBed.inject(LieuVersementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const lieuVersement: ILieuVersement = { id: 456 };

      activatedRoute.data = of({ lieuVersement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lieuVersement));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LieuVersement>>();
      const lieuVersement = { id: 123 };
      jest.spyOn(lieuVersementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lieuVersement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lieuVersement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lieuVersementService.update).toHaveBeenCalledWith(lieuVersement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LieuVersement>>();
      const lieuVersement = new LieuVersement();
      jest.spyOn(lieuVersementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lieuVersement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lieuVersement }));
      saveSubject.complete();

      // THEN
      expect(lieuVersementService.create).toHaveBeenCalledWith(lieuVersement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LieuVersement>>();
      const lieuVersement = { id: 123 };
      jest.spyOn(lieuVersementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lieuVersement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lieuVersementService.update).toHaveBeenCalledWith(lieuVersement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
