import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMotifListeDepense, MotifListeDepense } from '../motif-liste-depense.model';

import { MotifListeDepenseService } from './motif-liste-depense.service';

describe('MotifListeDepense Service', () => {
  let service: MotifListeDepenseService;
  let httpMock: HttpTestingController;
  let elemDefault: IMotifListeDepense;
  let expectedResult: IMotifListeDepense | IMotifListeDepense[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MotifListeDepenseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
      montant: 0,
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

    it('should create a MotifListeDepense', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MotifListeDepense()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MotifListeDepense', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          montant: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MotifListeDepense', () => {
      const patchObject = Object.assign(
        {
          montant: 1,
        },
        new MotifListeDepense()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MotifListeDepense', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          montant: 1,
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

    it('should delete a MotifListeDepense', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMotifListeDepenseToCollectionIfMissing', () => {
      it('should add a MotifListeDepense to an empty array', () => {
        const motifListeDepense: IMotifListeDepense = { id: 123 };
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing([], motifListeDepense);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifListeDepense);
      });

      it('should not add a MotifListeDepense to an array that contains it', () => {
        const motifListeDepense: IMotifListeDepense = { id: 123 };
        const motifListeDepenseCollection: IMotifListeDepense[] = [
          {
            ...motifListeDepense,
          },
          { id: 456 },
        ];
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing(motifListeDepenseCollection, motifListeDepense);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MotifListeDepense to an array that doesn't contain it", () => {
        const motifListeDepense: IMotifListeDepense = { id: 123 };
        const motifListeDepenseCollection: IMotifListeDepense[] = [{ id: 456 }];
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing(motifListeDepenseCollection, motifListeDepense);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifListeDepense);
      });

      it('should add only unique MotifListeDepense to an array', () => {
        const motifListeDepenseArray: IMotifListeDepense[] = [{ id: 123 }, { id: 456 }, { id: 53838 }];
        const motifListeDepenseCollection: IMotifListeDepense[] = [{ id: 123 }];
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing(motifListeDepenseCollection, ...motifListeDepenseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const motifListeDepense: IMotifListeDepense = { id: 123 };
        const motifListeDepense2: IMotifListeDepense = { id: 456 };
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing([], motifListeDepense, motifListeDepense2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifListeDepense);
        expect(expectedResult).toContain(motifListeDepense2);
      });

      it('should accept null and undefined values', () => {
        const motifListeDepense: IMotifListeDepense = { id: 123 };
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing([], null, motifListeDepense, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifListeDepense);
      });

      it('should return initial array if no MotifListeDepense is added', () => {
        const motifListeDepenseCollection: IMotifListeDepense[] = [{ id: 123 }];
        expectedResult = service.addMotifListeDepenseToCollectionIfMissing(motifListeDepenseCollection, undefined, null);
        expect(expectedResult).toEqual(motifListeDepenseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
