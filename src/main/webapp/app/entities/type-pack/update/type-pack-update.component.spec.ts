import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypePackService } from '../service/type-pack.service';
import { ITypePack, TypePack } from '../type-pack.model';

import { TypePackUpdateComponent } from './type-pack-update.component';

describe('TypePack Management Update Component', () => {
  let comp: TypePackUpdateComponent;
  let fixture: ComponentFixture<TypePackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typePackService: TypePackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypePackUpdateComponent],
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
      .overrideTemplate(TypePackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typePackService = TestBed.inject(TypePackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typePack: ITypePack = { id: 456 };

      activatedRoute.data = of({ typePack });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typePack));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePack>>();
      const typePack = { id: 123 };
      jest.spyOn(typePackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePack }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typePackService.update).toHaveBeenCalledWith(typePack);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePack>>();
      const typePack = new TypePack();
      jest.spyOn(typePackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePack }));
      saveSubject.complete();

      // THEN
      expect(typePackService.create).toHaveBeenCalledWith(typePack);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePack>>();
      const typePack = { id: 123 };
      jest.spyOn(typePackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePack });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typePackService.update).toHaveBeenCalledWith(typePack);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
