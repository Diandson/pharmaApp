import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VersementDetailComponent } from './versement-detail.component';

describe('Versement Management Detail Component', () => {
  let comp: VersementDetailComponent;
  let fixture: ComponentFixture<VersementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VersementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ versement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VersementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VersementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load versement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.versement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
