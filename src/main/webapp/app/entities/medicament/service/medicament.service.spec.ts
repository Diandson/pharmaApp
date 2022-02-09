import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMedicament, Medicament } from '../medicament.model';

import { MedicamentService } from './medicament.service';

describe('Medicament Service', () => {
  let service: MedicamentService;
  let httpMock: HttpTestingController;
  let elemDefault: IMedicament;
  let expectedResult: IMedicament | IMedicament[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedicamentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      denomination: 'AAAAAAA',
      dci: 'AAAAAAA',
      forme: 'AAAAAAA',
      dosage: 'AAAAAAA',
      classe: 'AAAAAAA',
      codeBare: 'AAAAAAA',
      prixAchat: 0,
      prixPublic: 0,
      stockAlerte: 0,
      stockSecurite: 0,
      stockTheorique: 0,
      dateFabrication: currentDate,
      dateExpiration: currentDate,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
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

    it('should create a Medicament', () => {
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

      service.create(new Medicament()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Medicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          dci: 'BBBBBB',
          forme: 'BBBBBB',
          dosage: 'BBBBBB',
          classe: 'BBBBBB',
          codeBare: 'BBBBBB',
          prixAchat: 1,
          prixPublic: 1,
          stockAlerte: 1,
          stockSecurite: 1,
          stockTheorique: 1,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
          image: 'BBBBBB',
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

    it('should partial update a Medicament', () => {
      const patchObject = Object.assign(
        {
          denomination: 'BBBBBB',
          forme: 'BBBBBB',
          stockSecurite: 1,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
        },
        new Medicament()
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

    it('should return a list of Medicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          dci: 'BBBBBB',
          forme: 'BBBBBB',
          dosage: 'BBBBBB',
          classe: 'BBBBBB',
          codeBare: 'BBBBBB',
          prixAchat: 1,
          prixPublic: 1,
          stockAlerte: 1,
          stockSecurite: 1,
          stockTheorique: 1,
          dateFabrication: currentDate.format(DATE_FORMAT),
          dateExpiration: currentDate.format(DATE_FORMAT),
          image: 'BBBBBB',
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

    it('should delete a Medicament', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMedicamentToCollectionIfMissing', () => {
      it('should add a Medicament to an empty array', () => {
        const medicament: IMedicament = { id: 123 };
        expectedResult = service.addMedicamentToCollectionIfMissing([], medicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicament);
      });

      it('should not add a Medicament to an array that contains it', () => {
        const medicament: IMedicament = { id: 123 };
        const medicamentCollection: IMedicament[] = [
          {
            ...medicament,
          },
          { id: 456 },
        ];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Medicament to an array that doesn't contain it", () => {
        const medicament: IMedicament = { id: 123 };
        const medicamentCollection: IMedicament[] = [{ id: 456 }];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicament);
      });

      it('should add only unique Medicament to an array', () => {
        const medicamentArray: IMedicament[] = [{ id: 123 }, { id: 456 }, { id: 20368 }];
        const medicamentCollection: IMedicament[] = [{ id: 123 }];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, ...medicamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicament: IMedicament = { id: 123 };
        const medicament2: IMedicament = { id: 456 };
        expectedResult = service.addMedicamentToCollectionIfMissing([], medicament, medicament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicament);
        expect(expectedResult).toContain(medicament2);
      });

      it('should accept null and undefined values', () => {
        const medicament: IMedicament = { id: 123 };
        expectedResult = service.addMedicamentToCollectionIfMissing([], null, medicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicament);
      });

      it('should return initial array if no Medicament is added', () => {
        const medicamentCollection: IMedicament[] = [{ id: 123 }];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, undefined, null);
        expect(expectedResult).toEqual(medicamentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
