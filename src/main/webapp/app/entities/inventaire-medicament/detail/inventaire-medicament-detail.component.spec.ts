import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InventaireMedicamentDetailComponent } from './inventaire-medicament-detail.component';

describe('InventaireMedicament Management Detail Component', () => {
  let comp: InventaireMedicamentDetailComponent;
  let fixture: ComponentFixture<InventaireMedicamentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventaireMedicamentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inventaireMedicament: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InventaireMedicamentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InventaireMedicamentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inventaireMedicament on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inventaireMedicament).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
