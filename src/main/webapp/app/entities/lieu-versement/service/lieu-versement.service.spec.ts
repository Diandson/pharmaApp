import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILieuVersement, LieuVersement } from '../lieu-versement.model';

import { LieuVersementService } from './lieu-versement.service';

describe('LieuVersement Service', () => {
  let service: LieuVersementService;
  let httpMock: HttpTestingController;
  let elemDefault: ILieuVersement;
  let expectedResult: ILieuVersement | ILieuVersement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LieuVersementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
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

    it('should create a LieuVersement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LieuVersement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LieuVersement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LieuVersement', () => {
      const patchObject = Object.assign({}, new LieuVersement());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LieuVersement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
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

    it('should delete a LieuVersement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLieuVersementToCollectionIfMissing', () => {
      it('should add a LieuVersement to an empty array', () => {
        const lieuVersement: ILieuVersement = { id: 123 };
        expectedResult = service.addLieuVersementToCollectionIfMissing([], lieuVersement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lieuVersement);
      });

      it('should not add a LieuVersement to an array that contains it', () => {
        const lieuVersement: ILieuVersement = { id: 123 };
        const lieuVersementCollection: ILieuVersement[] = [
          {
            ...lieuVersement,
          },
          { id: 456 },
        ];
        expectedResult = service.addLieuVersementToCollectionIfMissing(lieuVersementCollection, lieuVersement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LieuVersement to an array that doesn't contain it", () => {
        const lieuVersement: ILieuVersement = { id: 123 };
        const lieuVersementCollection: ILieuVersement[] = [{ id: 456 }];
        expectedResult = service.addLieuVersementToCollectionIfMissing(lieuVersementCollection, lieuVersement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lieuVersement);
      });

      it('should add only unique LieuVersement to an array', () => {
        const lieuVersementArray: ILieuVersement[] = [{ id: 123 }, { id: 456 }, { id: 17558 }];
        const lieuVersementCollection: ILieuVersement[] = [{ id: 123 }];
        expectedResult = service.addLieuVersementToCollectionIfMissing(lieuVersementCollection, ...lieuVersementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lieuVersement: ILieuVersement = { id: 123 };
        const lieuVersement2: ILieuVersement = { id: 456 };
        expectedResult = service.addLieuVersementToCollectionIfMissing([], lieuVersement, lieuVersement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lieuVersement);
        expect(expectedResult).toContain(lieuVersement2);
      });

      it('should accept null and undefined values', () => {
        const lieuVersement: ILieuVersement = { id: 123 };
        expectedResult = service.addLieuVersementToCollectionIfMissing([], null, lieuVersement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lieuVersement);
      });

      it('should return initial array if no LieuVersement is added', () => {
        const lieuVersementCollection: ILieuVersement[] = [{ id: 123 }];
        expectedResult = service.addLieuVersementToCollectionIfMissing(lieuVersementCollection, undefined, null);
        expect(expectedResult).toEqual(lieuVersementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
