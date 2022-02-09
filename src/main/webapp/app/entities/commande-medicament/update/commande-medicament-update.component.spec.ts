import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandeMedicamentService } from '../service/commande-medicament.service';
import { ICommandeMedicament, CommandeMedicament } from '../commande-medicament.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

import { CommandeMedicamentUpdateComponent } from './commande-medicament-update.component';

describe('CommandeMedicament Management Update Component', () => {
  let comp: CommandeMedicamentUpdateComponent;
  let fixture: ComponentFixture<CommandeMedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandeMedicamentService: CommandeMedicamentService;
  let medicamentService: MedicamentService;
  let commandeService: CommandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommandeMedicamentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommandeMedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeMedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandeMedicamentService = TestBed.inject(CommandeMedicamentService);
    medicamentService = TestBed.inject(MedicamentService);
    commandeService = TestBed.inject(CommandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medicament query and add missing value', () => {
      const commandeMedicament: ICommandeMedicament = { id: 456 };
      const medicament: IMedicament = { id: 41640 };
      commandeMedicament.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 56811 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(medicamentCollection, ...additionalMedicaments);
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commande query and add missing value', () => {
      const commandeMedicament: ICommandeMedicament = { id: 456 };
      const commande: ICommande = { id: 16034 };
      commandeMedicament.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 28667 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commandeMedicament: ICommandeMedicament = { id: 456 };
      const medicament: IMedicament = { id: 76618 };
      commandeMedicament.medicament = medicament;
      const commande: ICommande = { id: 91795 };
      commandeMedicament.commande = commande;

      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commandeMedicament));
      expect(comp.medicamentsSharedCollection).toContain(medicament);
      expect(comp.commandesSharedCollection).toContain(commande);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeMedicament>>();
      const commandeMedicament = { id: 123 };
      jest.spyOn(commandeMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandeMedicament }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandeMedicamentService.update).toHaveBeenCalledWith(commandeMedicament);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeMedicament>>();
      const commandeMedicament = new CommandeMedicament();
      jest.spyOn(commandeMedicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandeMedicament }));
      saveSubject.complete();

      // THEN
      expect(commandeMedicamentService.create).toHaveBeenCalledWith(commandeMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeMedicament>>();
      const commandeMedicament = { id: 123 };
      jest.spyOn(commandeMedicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeMedicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandeMedicamentService.update).toHaveBeenCalledWith(commandeMedicament);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMedicamentById', () => {
      it('Should return tracked Medicament primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMedicamentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCommandeById', () => {
      it('Should return tracked Commande primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommandeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
