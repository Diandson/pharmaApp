import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MotifListeDepenseDetailComponent } from './motif-liste-depense-detail.component';

describe('MotifListeDepense Management Detail Component', () => {
  let comp: MotifListeDepenseDetailComponent;
  let fixture: ComponentFixture<MotifListeDepenseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MotifListeDepenseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ motifListeDepense: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MotifListeDepenseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MotifListeDepenseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load motifListeDepense on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.motifListeDepense).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
