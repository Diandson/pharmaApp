import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApprovisionnementMedicament, ApprovisionnementMedicament } from '../approvisionnement-medicament.model';

import { ApprovisionnementMedicamentService } from './approvisionnement-medicament.service';

describe('ApprovisionnementMedicament Service', () => {
  let service: ApprovisionnementMedicamentService;
  let httpMock: HttpTestingController;
  let elemDefault: IApprovisionnementMedicament;
  let expectedResult: IApprovisionnementMedicament | IApprovisionnementMedicament[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApprovisionnementMedicamentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      quantite: 0,
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

    it('should create a ApprovisionnementMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ApprovisionnementMedicament()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApprovisionnementMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApprovisionnementMedicament', () => {
      const patchObject = Object.assign({}, new ApprovisionnementMedicament());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApprovisionnementMedicament', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantite: 1,
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

    it('should delete a ApprovisionnementMedicament', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApprovisionnementMedicamentToCollectionIfMissing', () => {
      it('should add a ApprovisionnementMedicament to an empty array', () => {
        const approvisionnementMedicament: IApprovisionnementMedicament = { id: 123 };
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing([], approvisionnementMedicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(approvisionnementMedicament);
      });

      it('should not add a ApprovisionnementMedicament to an array that contains it', () => {
        const approvisionnementMedicament: IApprovisionnementMedicament = { id: 123 };
        const approvisionnementMedicamentCollection: IApprovisionnementMedicament[] = [
          {
            ...approvisionnementMedicament,
          },
          { id: 456 },
        ];
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing(
          approvisionnementMedicamentCollection,
          approvisionnementMedicament
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApprovisionnementMedicament to an array that doesn't contain it", () => {
        const approvisionnementMedicament: IApprovisionnementMedicament = { id: 123 };
        const approvisionnementMedicamentCollection: IApprovisionnementMedicament[] = [{ id: 456 }];
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing(
          approvisionnementMedicamentCollection,
          approvisionnementMedicament
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(approvisionnementMedicament);
      });

      it('should add only unique ApprovisionnementMedicament to an array', () => {
        const approvisionnementMedicamentArray: IApprovisionnementMedicament[] = [{ id: 123 }, { id: 456 }, { id: 51245 }];
        const approvisionnementMedicamentCollection: IApprovisionnementMedicament[] = [{ id: 123 }];
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing(
          approvisionnementMedicamentCollection,
          ...approvisionnementMedicamentArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const approvisionnementMedicament: IApprovisionnementMedicament = { id: 123 };
        const approvisionnementMedicament2: IApprovisionnementMedicament = { id: 456 };
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing(
          [],
          approvisionnementMedicament,
          approvisionnementMedicament2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(approvisionnementMedicament);
        expect(expectedResult).toContain(approvisionnementMedicament2);
      });

      it('should accept null and undefined values', () => {
        const approvisionnementMedicament: IApprovisionnementMedicament = { id: 123 };
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing([], null, approvisionnementMedicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(approvisionnementMedicament);
      });

      it('should return initial array if no ApprovisionnementMedicament is added', () => {
        const approvisionnementMedicamentCollection: IApprovisionnementMedicament[] = [{ id: 123 }];
        expectedResult = service.addApprovisionnementMedicamentToCollectionIfMissing(
          approvisionnementMedicamentCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(approvisionnementMedicamentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
