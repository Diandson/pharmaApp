import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommandeMedicament, CommandeMedicament } from '../commande-medicament.model';

import { CommandeMedicamentService } from './commande-medicament.service';

describe('CommandeMedicament Service', () => {
  let service: CommandeMedicamentService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommandeMedicament;
  let expectedResult: ICommandeMedicament | ICommandeMedicament[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommandeMedicamentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      quantite: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CommandeMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CommandeMedicament()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommandeMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommandeMedicament', () => {
      const patchObject = Object.assign(
        {
          quantite: 1,
        },
        new CommandeMedicament()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommandeMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CommandeMedicament', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommandeMedicamentToCollectionIfMissing', () => {
      it('should add a CommandeMedicament to an empty array', () => {
        const commandeMedicament: ICommandeMedicament = { id: 123 };
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing([], commandeMedicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandeMedicament);
      });

      it('should not add a CommandeMedicament to an array that contains it', () => {
        const commandeMedicament: ICommandeMedicament = { id: 123 };
        const commandeMedicamentCollection: ICommandeMedicament[] = [
          {
            ...commandeMedicament,
          },
          { id: 456 },
        ];
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing(commandeMedicamentCollection, commandeMedicament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommandeMedicament to an array that doesn't contain it", () => {
        const commandeMedicament: ICommandeMedicament = { id: 123 };
        const commandeMedicamentCollection: ICommandeMedicament[] = [{ id: 456 }];
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing(commandeMedicamentCollection, commandeMedicament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandeMedicament);
      });

      it('should add only unique CommandeMedicament to an array', () => {
        const commandeMedicamentArray: ICommandeMedicament[] = [{ id: 123 }, { id: 456 }, { id: 45534 }];
        const commandeMedicamentCollection: ICommandeMedicament[] = [{ id: 123 }];
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing(commandeMedicamentCollection, ...commandeMedicamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commandeMedicament: ICommandeMedicament = { id: 123 };
        const commandeMedicament2: ICommandeMedicament = { id: 456 };
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing([], commandeMedicament, commandeMedicament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandeMedicament);
        expect(expectedResult).toContain(commandeMedicament2);
      });

      it('should accept null and undefined values', () => {
        const commandeMedicament: ICommandeMedicament = { id: 123 };
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing([], null, commandeMedicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandeMedicament);
      });

      it('should return initial array if no CommandeMedicament is added', () => {
        const commandeMedicamentCollection: ICommandeMedicament[] = [{ id: 123 }];
        expectedResult = service.addCommandeMedicamentToCollectionIfMissing(commandeMedicamentCollection, undefined, null);
        expect(expectedResult).toEqual(commandeMedicamentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
