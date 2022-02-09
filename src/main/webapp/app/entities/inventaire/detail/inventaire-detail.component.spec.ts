import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InventaireDetailComponent } from './inventaire-detail.component';

describe('Inventaire Management Detail Component', () => {
  let comp: InventaireDetailComponent;
  let fixture: ComponentFixture<InventaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inventaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InventaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InventaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inventaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inventaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
