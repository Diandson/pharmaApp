import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILivraison, Livraison } from '../livraison.model';

import { LivraisonService } from './livraison.service';

describe('Livraison Service', () => {
  let service: LivraisonService;
  let httpMock: HttpTestingController;
  let elemDefault: ILivraison;
  let expectedResult: ILivraison | ILivraison[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LivraisonService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numero: 'AAAAAAA',
      dateLivraison: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Livraison', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.create(new Livraison()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Livraison', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Livraison', () => {
      const patchObject = Object.assign({}, new Livraison());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Livraison', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          dateLivraison: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateLivraison: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Livraison', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLivraisonToCollectionIfMissing', () => {
      it('should add a Livraison to an empty array', () => {
        const livraison: ILivraison = { id: 123 };
        expectedResult = service.addLivraisonToCollectionIfMissing([], livraison);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livraison);
      });

      it('should not add a Livraison to an array that contains it', () => {
        const livraison: ILivraison = { id: 123 };
        const livraisonCollection: ILivraison[] = [
          {
            ...livraison,
          },
          { id: 456 },
        ];
        expectedResult = service.addLivraisonToCollectionIfMissing(livraisonCollection, livraison);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Livraison to an array that doesn't contain it", () => {
        const livraison: ILivraison = { id: 123 };
        const livraisonCollection: ILivraison[] = [{ id: 456 }];
        expectedResult = service.addLivraisonToCollectionIfMissing(livraisonCollection, livraison);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livraison);
      });

      it('should add only unique Livraison to an array', () => {
        const livraisonArray: ILivraison[] = [{ id: 123 }, { id: 456 }, { id: 69447 }];
        const livraisonCollection: ILivraison[] = [{ id: 123 }];
        expectedResult = service.addLivraisonToCollectionIfMissing(livraisonCollection, ...livraisonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const livraison: ILivraison = { id: 123 };
        const livraison2: ILivraison = { id: 456 };
        expectedResult = service.addLivraisonToCollectionIfMissing([], livraison, livraison2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livraison);
        expect(expectedResult).toContain(livraison2);
      });

      it('should accept null and undefined values', () => {
        const livraison: ILivraison = { id: 123 };
        expectedResult = service.addLivraisonToCollectionIfMissing([], null, livraison, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livraison);
      });

      it('should return initial array if no Livraison is added', () => {
        const livraisonCollection: ILivraison[] = [{ id: 123 }];
        expectedResult = service.addLivraisonToCollectionIfMissing(livraisonCollection, undefined, null);
        expect(expectedResult).toEqual(livraisonCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
