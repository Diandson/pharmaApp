import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LieuVersementDetailComponent } from './lieu-versement-detail.component';

describe('LieuVersement Management Detail Component', () => {
  let comp: LieuVersementDetailComponent;
  let fixture: ComponentFixture<LieuVersementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LieuVersementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lieuVersement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LieuVersementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LieuVersementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lieuVersement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lieuVersement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
