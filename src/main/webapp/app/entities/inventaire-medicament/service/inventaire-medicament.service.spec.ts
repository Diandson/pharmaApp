import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInventaireMedicament, InventaireMedicament } from '../inventaire-medicament.model';

import { InventaireMedicamentService } from './inventaire-medicament.service';

describe('InventaireMedicament Service', () => {
  let service: InventaireMedicamentService;
  let httpMock: HttpTestingController;
  let elemDefault: IInventaireMedicament;
  let expectedResult: IInventaireMedicament | IInventaireMedicament[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InventaireMedicamentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      stockTheorique: 0,
      stockPhysique: 0,
      stockDifferent: 0,
      dateFabrication: currentDate,
      dateExpiration: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InventaireMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFabrication: currentDate,
          dateExpiration: currentDate,
        },
        returnedFromService
      );

      service.create(new InventaireMedicament()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventaireMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          stockTheorique: 1,
          stockPhysique: 1,
          stockDifferent: 1,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFabrication: currentDate,
          dateExpiration: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventaireMedicament', () => {
      const patchObject = Object.assign(
        {
          stockTheorique: 1,
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        new InventaireMedicament()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateFabrication: currentDate,
          dateExpiration: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventaireMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          stockTheorique: 1,
          stockPhysique: 1,
          stockDifferent: 1,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFabrication: currentDate,
          dateExpiration: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InventaireMedicament', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInventaireMedicamentToCollectionIfMissing', () => {
      it('should add a InventaireMedicament to an empty array', () => {
        const inventaireMedicament: IInventaireMedicament = { id: 123 };
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing([], inventaireMedicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventaireMedicament);
      });

      it('should not add a InventaireMedicament to an array that contains it', () => {
        const inventaireMedicament: IInventaireMedicament = { id: 123 };
        const inventaireMedicamentCollection: IInventaireMedicament[] = [
          {
            ...inventaireMedicament,
          },
          { id: 456 },
        ];
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing(inventaireMedicamentCollection, inventaireMedicament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventaireMedicament to an array that doesn't contain it", () => {
        const inventaireMedicament: IInventaireMedicament = { id: 123 };
        const inventaireMedicamentCollection: IInventaireMedicament[] = [{ id: 456 }];
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing(inventaireMedicamentCollection, inventaireMedicament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventaireMedicament);
      });

      it('should add only unique InventaireMedicament to an array', () => {
        const inventaireMedicamentArray: IInventaireMedicament[] = [{ id: 123 }, { id: 456 }, { id: 32953 }];
        const inventaireMedicamentCollection: IInventaireMedicament[] = [{ id: 123 }];
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing(inventaireMedicamentCollection, ...inventaireMedicamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventaireMedicament: IInventaireMedicament = { id: 123 };
        const inventaireMedicament2: IInventaireMedicament = { id: 456 };
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing([], inventaireMedicament, inventaireMedicament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventaireMedicament);
        expect(expectedResult).toContain(inventaireMedicament2);
      });

      it('should accept null and undefined values', () => {
        const inventaireMedicament: IInventaireMedicament = { id: 123 };
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing([], null, inventaireMedicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventaireMedicament);
      });

      it('should return initial array if no InventaireMedicament is added', () => {
        const inventaireMedicamentCollection: IInventaireMedicament[] = [{ id: 123 }];
        expectedResult = service.addInventaireMedicamentToCollectionIfMissing(inventaireMedicamentCollection, undefined, null);
        expect(expectedResult).toEqual(inventaireMedicamentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
