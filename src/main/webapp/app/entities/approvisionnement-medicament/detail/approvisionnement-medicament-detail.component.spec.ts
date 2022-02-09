import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovisionnementMedicamentDetailComponent } from './approvisionnement-medicament-detail.component';

describe('ApprovisionnementMedicament Management Detail Component', () => {
  let comp: ApprovisionnementMedicamentDetailComponent;
  let fixture: ComponentFixture<ApprovisionnementMedicamentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApprovisionnementMedicamentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ approvisionnementMedicament: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApprovisionnementMedicamentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApprovisionnementMedicamentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load approvisionnementMedicament on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.approvisionnementMedicament).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
