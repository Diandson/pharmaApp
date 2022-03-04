import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVersement, Versement } from '../versement.model';

import { VersementService } from './versement.service';

describe('Versement Service', () => {
  let service: VersementService;
  let httpMock: HttpTestingController;
  let elemDefault: IVersement;
  let expectedResult: IVersement | IVersement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VersementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numero: 'AAAAAAA',
      commentaire: 'AAAAAAA',
      montant: 0,
      resteAVerser: 0,
      lieuVersement: 'AAAAAAA',
      referenceVersement: 'AAAAAAA',
      identiteReceveur: 'AAAAAAA',
      dateVersement: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateVersement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Versement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateVersement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVersement: currentDate,
        },
        returnedFromService
      );

      service.create(new Versement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Versement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          commentaire: 'BBBBBB',
          montant: 1,
          resteAVerser: 1,
          lieuVersement: 'BBBBBB',
          referenceVersement: 'BBBBBB',
          identiteReceveur: 'BBBBBB',
          dateVersement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVersement: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Versement', () => {
      const patchObject = Object.assign(
        {
          commentaire: 'BBBBBB',
          montant: 1,
          lieuVersement: 'BBBBBB',
          identiteReceveur: 'BBBBBB',
          dateVersement: currentDate.format(DATE_TIME_FORMAT),
        },
        new Versement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateVersement: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Versement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          commentaire: 'BBBBBB',
          montant: 1,
          resteAVerser: 1,
          lieuVersement: 'BBBBBB',
          referenceVersement: 'BBBBBB',
          identiteReceveur: 'BBBBBB',
          dateVersement: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVersement: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Versement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVersementToCollectionIfMissing', () => {
      it('should add a Versement to an empty array', () => {
        const versement: IVersement = { id: 123 };
        expectedResult = service.addVersementToCollectionIfMissing([], versement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(versement);
      });

      it('should not add a Versement to an array that contains it', () => {
        const versement: IVersement = { id: 123 };
        const versementCollection: IVersement[] = [
          {
            ...versement,
          },
          { id: 456 },
        ];
        expectedResult = service.addVersementToCollectionIfMissing(versementCollection, versement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Versement to an array that doesn't contain it", () => {
        const versement: IVersement = { id: 123 };
        const versementCollection: IVersement[] = [{ id: 456 }];
        expectedResult = service.addVersementToCollectionIfMissing(versementCollection, versement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(versement);
      });

      it('should add only unique Versement to an array', () => {
        const versementArray: IVersement[] = [{ id: 123 }, { id: 456 }, { id: 95554 }];
        const versementCollection: IVersement[] = [{ id: 123 }];
        expectedResult = service.addVersementToCollectionIfMissing(versementCollection, ...versementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const versement: IVersement = { id: 123 };
        const versement2: IVersement = { id: 456 };
        expectedResult = service.addVersementToCollectionIfMissing([], versement, versement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(versement);
        expect(expectedResult).toContain(versement2);
      });

      it('should accept null and undefined values', () => {
        const versement: IVersement = { id: 123 };
        expectedResult = service.addVersementToCollectionIfMissing([], null, versement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(versement);
      });

      it('should return initial array if no Versement is added', () => {
        const versementCollection: IVersement[] = [{ id: 123 }];
        expectedResult = service.addVersementToCollectionIfMissing(versementCollection, undefined, null);
        expect(expectedResult).toEqual(versementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
