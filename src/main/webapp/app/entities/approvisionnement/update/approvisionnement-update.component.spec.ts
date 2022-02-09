import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApprovisionnementService } from '../service/approvisionnement.service';
import { IApprovisionnement, Approvisionnement } from '../approvisionnement.model';

import { ApprovisionnementUpdateComponent } from './approvisionnement-update.component';

describe('Approvisionnement Management Update Component', () => {
  let comp: ApprovisionnementUpdateComponent;
  let fixture: ComponentFixture<ApprovisionnementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let approvisionnementService: ApprovisionnementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApprovisionnementUpdateComponent],
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
      .overrideTemplate(ApprovisionnementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApprovisionnementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    approvisionnementService = TestBed.inject(ApprovisionnementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const approvisionnement: IApprovisionnement = { id: 456 };

      activatedRoute.data = of({ approvisionnement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(approvisionnement));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Approvisionnement>>();
      const approvisionnement = { id: 123 };
      jest.spyOn(approvisionnementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvisionnement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(approvisionnementService.update).toHaveBeenCalledWith(approvisionnement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Approvisionnement>>();
      const approvisionnement = new Approvisionnement();
      jest.spyOn(approvisionnementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvisionnement }));
      saveSubject.complete();

      // THEN
      expect(approvisionnementService.create).toHaveBeenCalledWith(approvisionnement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Approvisionnement>>();
      const approvisionnement = { id: 123 };
      jest.spyOn(approvisionnementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvisionnement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(approvisionnementService.update).toHaveBeenCalledWith(approvisionnement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
