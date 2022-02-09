import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovisionnementDetailComponent } from './approvisionnement-detail.component';

describe('Approvisionnement Management Detail Component', () => {
  let comp: ApprovisionnementDetailComponent;
  let fixture: ComponentFixture<ApprovisionnementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApprovisionnementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ approvisionnement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApprovisionnementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApprovisionnementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load approvisionnement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.approvisionnement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
