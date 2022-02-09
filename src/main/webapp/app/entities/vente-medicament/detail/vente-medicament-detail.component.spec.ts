import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VenteMedicamentDetailComponent } from './vente-medicament-detail.component';

describe('VenteMedicament Management Detail Component', () => {
  let comp: VenteMedicamentDetailComponent;
  let fixture: ComponentFixture<VenteMedicamentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VenteMedicamentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ venteMedicament: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VenteMedicamentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VenteMedicamentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load venteMedicament on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.venteMedicament).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
