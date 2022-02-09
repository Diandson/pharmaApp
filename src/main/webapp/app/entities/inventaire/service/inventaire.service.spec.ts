import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInventaire, Inventaire } from '../inventaire.model';

import { InventaireService } from './inventaire.service';

describe('Inventaire Service', () => {
  let service: InventaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IInventaire;
  let expectedResult: IInventaire | IInventaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InventaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numero: 'AAAAAAA',
      dateInventaire: 'AAAAAAA',
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

    it('should create a Inventaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Inventaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inventaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          dateInventaire: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inventaire', () => {
      const patchObject = Object.assign(
        {
          numero: 'BBBBBB',
        },
        new Inventaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inventaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          dateInventaire: 'BBBBBB',
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

    it('should delete a Inventaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInventaireToCollectionIfMissing', () => {
      it('should add a Inventaire to an empty array', () => {
        const inventaire: IInventaire = { id: 123 };
        expectedResult = service.addInventaireToCollectionIfMissing([], inventaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventaire);
      });

      it('should not add a Inventaire to an array that contains it', () => {
        const inventaire: IInventaire = { id: 123 };
        const inventaireCollection: IInventaire[] = [
          {
            ...inventaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, inventaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inventaire to an array that doesn't contain it", () => {
        const inventaire: IInventaire = { id: 123 };
        const inventaireCollection: IInventaire[] = [{ id: 456 }];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, inventaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventaire);
      });

      it('should add only unique Inventaire to an array', () => {
        const inventaireArray: IInventaire[] = [{ id: 123 }, { id: 456 }, { id: 75760 }];
        const inventaireCollection: IInventaire[] = [{ id: 123 }];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, ...inventaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventaire: IInventaire = { id: 123 };
        const inventaire2: IInventaire = { id: 456 };
        expectedResult = service.addInventaireToCollectionIfMissing([], inventaire, inventaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventaire);
        expect(expectedResult).toContain(inventaire2);
      });

      it('should accept null and undefined values', () => {
        const inventaire: IInventaire = { id: 123 };
        expectedResult = service.addInventaireToCollectionIfMissing([], null, inventaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventaire);
      });

      it('should return initial array if no Inventaire is added', () => {
        const inventaireCollection: IInventaire[] = [{ id: 123 }];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, undefined, null);
        expect(expectedResult).toEqual(inventaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
