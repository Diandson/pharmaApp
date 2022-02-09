import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPack, Pack } from '../pack.model';

import { PackService } from './pack.service';

describe('Pack Service', () => {
  let service: PackService;
  let httpMock: HttpTestingController;
  let elemDefault: IPack;
  let expectedResult: IPack | IPack[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PackService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
      durer: 0,
      valide: false,
      dateRenew: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateRenew: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Pack', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateRenew: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateRenew: currentDate,
        },
        returnedFromService
      );

      service.create(new Pack()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pack', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          durer: 1,
          valide: true,
          dateRenew: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateRenew: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pack', () => {
      const patchObject = Object.assign(
        {
          dateRenew: currentDate.format(DATE_TIME_FORMAT),
        },
        new Pack()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateRenew: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pack', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          durer: 1,
          valide: true,
          dateRenew: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateRenew: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Pack', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPackToCollectionIfMissing', () => {
      it('should add a Pack to an empty array', () => {
        const pack: IPack = { id: 123 };
        expectedResult = service.addPackToCollectionIfMissing([], pack);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pack);
      });

      it('should not add a Pack to an array that contains it', () => {
        const pack: IPack = { id: 123 };
        const packCollection: IPack[] = [
          {
            ...pack,
          },
          { id: 456 },
        ];
        expectedResult = service.addPackToCollectionIfMissing(packCollection, pack);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pack to an array that doesn't contain it", () => {
        const pack: IPack = { id: 123 };
        const packCollection: IPack[] = [{ id: 456 }];
        expectedResult = service.addPackToCollectionIfMissing(packCollection, pack);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pack);
      });

      it('should add only unique Pack to an array', () => {
        const packArray: IPack[] = [{ id: 123 }, { id: 456 }, { id: 85318 }];
        const packCollection: IPack[] = [{ id: 123 }];
        expectedResult = service.addPackToCollectionIfMissing(packCollection, ...packArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pack: IPack = { id: 123 };
        const pack2: IPack = { id: 456 };
        expectedResult = service.addPackToCollectionIfMissing([], pack, pack2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pack);
        expect(expectedResult).toContain(pack2);
      });

      it('should accept null and undefined values', () => {
        const pack: IPack = { id: 123 };
        expectedResult = service.addPackToCollectionIfMissing([], null, pack, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pack);
      });

      it('should return initial array if no Pack is added', () => {
        const packCollection: IPack[] = [{ id: 123 }];
        expectedResult = service.addPackToCollectionIfMissing(packCollection, undefined, null);
        expect(expectedResult).toEqual(packCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
