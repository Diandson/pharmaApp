import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommandeMedicamentDetailComponent } from './commande-medicament-detail.component';

describe('CommandeMedicament Management Detail Component', () => {
  let comp: CommandeMedicamentDetailComponent;
  let fixture: ComponentFixture<CommandeMedicamentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommandeMedicamentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commandeMedicament: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CommandeMedicamentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommandeMedicamentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commandeMedicament on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commandeMedicament).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
