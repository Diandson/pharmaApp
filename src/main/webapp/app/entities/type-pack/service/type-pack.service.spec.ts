import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypePack, TypePack } from '../type-pack.model';

import { TypePackService } from './type-pack.service';

describe('TypePack Service', () => {
  let service: TypePackService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypePack;
  let expectedResult: ITypePack | ITypePack[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypePackService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
      durer: 0,
      prix: 0,
      annexe: 0,
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

    it('should create a TypePack', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TypePack()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypePack', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          durer: 1,
          prix: 1,
          annexe: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypePack', () => {
      const patchObject = Object.assign(
        {
          libelle: 'BBBBBB',
          durer: 1,
          prix: 1,
        },
        new TypePack()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypePack', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          durer: 1,
          prix: 1,
          annexe: 1,
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

    it('should delete a TypePack', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypePackToCollectionIfMissing', () => {
      it('should add a TypePack to an empty array', () => {
        const typePack: ITypePack = { id: 123 };
        expectedResult = service.addTypePackToCollectionIfMissing([], typePack);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePack);
      });

      it('should not add a TypePack to an array that contains it', () => {
        const typePack: ITypePack = { id: 123 };
        const typePackCollection: ITypePack[] = [
          {
            ...typePack,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypePackToCollectionIfMissing(typePackCollection, typePack);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypePack to an array that doesn't contain it", () => {
        const typePack: ITypePack = { id: 123 };
        const typePackCollection: ITypePack[] = [{ id: 456 }];
        expectedResult = service.addTypePackToCollectionIfMissing(typePackCollection, typePack);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePack);
      });

      it('should add only unique TypePack to an array', () => {
        const typePackArray: ITypePack[] = [{ id: 123 }, { id: 456 }, { id: 36779 }];
        const typePackCollection: ITypePack[] = [{ id: 123 }];
        expectedResult = service.addTypePackToCollectionIfMissing(typePackCollection, ...typePackArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typePack: ITypePack = { id: 123 };
        const typePack2: ITypePack = { id: 456 };
        expectedResult = service.addTypePackToCollectionIfMissing([], typePack, typePack2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePack);
        expect(expectedResult).toContain(typePack2);
      });

      it('should accept null and undefined values', () => {
        const typePack: ITypePack = { id: 123 };
        expectedResult = service.addTypePackToCollectionIfMissing([], null, typePack, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePack);
      });

      it('should return initial array if no TypePack is added', () => {
        const typePackCollection: ITypePack[] = [{ id: 123 }];
        expectedResult = service.addTypePackToCollectionIfMissing(typePackCollection, undefined, null);
        expect(expectedResult).toEqual(typePackCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
