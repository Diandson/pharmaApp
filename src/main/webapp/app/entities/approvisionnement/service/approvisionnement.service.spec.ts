import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApprovisionnement, Approvisionnement } from '../approvisionnement.model';

import { ApprovisionnementService } from './approvisionnement.service';

describe('Approvisionnement Service', () => {
  let service: ApprovisionnementService;
  let httpMock: HttpTestingController;
  let elemDefault: IApprovisionnement;
  let expectedResult: IApprovisionnement | IApprovisionnement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApprovisionnementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numero: 'AAAAAAA',
      agenceApp: 'AAAAAAA',
      commentaire: 'AAAAAAA',
      dateCommande: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateCommande: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Approvisionnement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateCommande: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCommande: currentDate,
        },
        returnedFromService
      );

      service.create(new Approvisionnement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Approvisionnement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          agenceApp: 'BBBBBB',
          commentaire: 'BBBBBB',
          dateCommande: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCommande: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Approvisionnement', () => {
      const patchObject = Object.assign(
        {
          numero: 'BBBBBB',
          agenceApp: 'BBBBBB',
        },
        new Approvisionnement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateCommande: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Approvisionnement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numero: 'BBBBBB',
          agenceApp: 'BBBBBB',
          commentaire: 'BBBBBB',
          dateCommande: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCommande: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Approvisionnement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApprovisionnementToCollectionIfMissing', () => {
      it('should add a Approvisionnement to an empty array', () => {
        const approvisionnement: IApprovisionnement = { id: 123 };
        expectedResult = service.addApprovisionnementToCollectionIfMissing([], approvisionnement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(approvisionnement);
      });

      it('should not add a Approvisionnement to an array that contains it', () => {
        const approvisionnement: IApprovisionnement = { id: 123 };
        const approvisionnementCollection: IApprovisionnement[] = [
          {
            ...approvisionnement,
          },
          { id: 456 },
        ];
        expectedResult = service.addApprovisionnementToCollectionIfMissing(approvisionnementCollection, approvisionnement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Approvisionnement to an array that doesn't contain it", () => {
        const approvisionnement: IApprovisionnement = { id: 123 };
        const approvisionnementCollection: IApprovisionnement[] = [{ id: 456 }];
        expectedResult = service.addApprovisionnementToCollectionIfMissing(approvisionnementCollection, approvisionnement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(approvisionnement);
      });

      it('should add only unique Approvisionnement to an array', () => {
        const approvisionnementArray: IApprovisionnement[] = [{ id: 123 }, { id: 456 }, { id: 72482 }];
        const approvisionnementCollection: IApprovisionnement[] = [{ id: 123 }];
        expectedResult = service.addApprovisionnementToCollectionIfMissing(approvisionnementCollection, ...approvisionnementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const approvisionnement: IApprovisionnement = { id: 123 };
        const approvisionnement2: IApprovisionnement = { id: 456 };
        expectedResult = service.addApprovisionnementToCollectionIfMissing([], approvisionnement, approvisionnement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(approvisionnement);
        expect(expectedResult).toContain(approvisionnement2);
      });

      it('should accept null and undefined values', () => {
        const approvisionnement: IApprovisionnement = { id: 123 };
        expectedResult = service.addApprovisionnementToCollectionIfMissing([], null, approvisionnement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(approvisionnement);
      });

      it('should return initial array if no Approvisionnement is added', () => {
        const approvisionnementCollection: IApprovisionnement[] = [{ id: 123 }];
        expectedResult = service.addApprovisionnementToCollectionIfMissing(approvisionnementCollection, undefined, null);
        expect(expectedResult).toEqual(approvisionnementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
