import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVenteMedicament, VenteMedicament } from '../vente-medicament.model';

import { VenteMedicamentService } from './vente-medicament.service';

describe('VenteMedicament Service', () => {
  let service: VenteMedicamentService;
  let httpMock: HttpTestingController;
  let elemDefault: IVenteMedicament;
  let expectedResult: IVenteMedicament | IVenteMedicament[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VenteMedicamentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      quantite: 0,
      montant: 'AAAAAAA',
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

    it('should create a VenteMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new VenteMedicament()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VenteMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
          montant: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VenteMedicament', () => {
      const patchObject = Object.assign(
        {
          quantite: 1,
          montant: 'BBBBBB',
        },
        new VenteMedicament()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VenteMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
          montant: 'BBBBBB',
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

    it('should delete a VenteMedicament', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVenteMedicamentToCollectionIfMissing', () => {
      it('should add a VenteMedicament to an empty array', () => {
        const venteMedicament: IVenteMedicament = { id: 123 };
        expectedResult = service.addVenteMedicamentToCollectionIfMissing([], venteMedicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venteMedicament);
      });

      it('should not add a VenteMedicament to an array that contains it', () => {
        const venteMedicament: IVenteMedicament = { id: 123 };
        const venteMedicamentCollection: IVenteMedicament[] = [
          {
            ...venteMedicament,
          },
          { id: 456 },
        ];
        expectedResult = service.addVenteMedicamentToCollectionIfMissing(venteMedicamentCollection, venteMedicament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VenteMedicament to an array that doesn't contain it", () => {
        const venteMedicament: IVenteMedicament = { id: 123 };
        const venteMedicamentCollection: IVenteMedicament[] = [{ id: 456 }];
        expectedResult = service.addVenteMedicamentToCollectionIfMissing(venteMedicamentCollection, venteMedicament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venteMedicament);
      });

      it('should add only unique VenteMedicament to an array', () => {
        const venteMedicamentArray: IVenteMedicament[] = [{ id: 123 }, { id: 456 }, { id: 57656 }];
        const venteMedicamentCollection: IVenteMedicament[] = [{ id: 123 }];
        expectedResult = service.addVenteMedicamentToCollectionIfMissing(venteMedicamentCollection, ...venteMedicamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const venteMedicament: IVenteMedicament = { id: 123 };
        const venteMedicament2: IVenteMedicament = { id: 456 };
        expectedResult = service.addVenteMedicamentToCollectionIfMissing([], venteMedicament, venteMedicament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venteMedicament);
        expect(expectedResult).toContain(venteMedicament2);
      });

      it('should accept null and undefined values', () => {
        const venteMedicament: IVenteMedicament = { id: 123 };
        expectedResult = service.addVenteMedicamentToCollectionIfMissing([], null, venteMedicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venteMedicament);
      });

      it('should return initial array if no VenteMedicament is added', () => {
        const venteMedicamentCollection: IVenteMedicament[] = [{ id: 123 }];
        expectedResult = service.addVenteMedicamentToCollectionIfMissing(venteMedicamentCollection, undefined, null);
        expect(expectedResult).toEqual(venteMedicamentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
