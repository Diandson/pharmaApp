import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssuranceDetailComponent } from './assurance-detail.component';

describe('Assurance Management Detail Component', () => {
  let comp: AssuranceDetailComponent;
  let fixture: ComponentFixture<AssuranceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssuranceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assurance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssuranceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssuranceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assurance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assurance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
