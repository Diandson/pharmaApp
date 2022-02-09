import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypePackDetailComponent } from './type-pack-detail.component';

describe('TypePack Management Detail Component', () => {
  let comp: TypePackDetailComponent;
  let fixture: ComponentFixture<TypePackDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypePackDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typePack: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypePackDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypePackDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typePack on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typePack).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
