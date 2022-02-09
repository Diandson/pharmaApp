import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAssurance, Assurance } from '../assurance.model';

import { AssuranceService } from './assurance.service';

describe('Assurance Service', () => {
  let service: AssuranceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssurance;
  let expectedResult: IAssurance | IAssurance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssuranceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
      taux: 0,
      email: 'AAAAAAA',
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

    it('should create a Assurance', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Assurance()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Assurance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          taux: 1,
          email: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Assurance', () => {
      const patchObject = Object.assign(
        {
          libelle: 'BBBBBB',
          email: 'BBBBBB',
        },
        new Assurance()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Assurance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
          taux: 1,
          email: 'BBBBBB',
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

    it('should delete a Assurance', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssuranceToCollectionIfMissing', () => {
      it('should add a Assurance to an empty array', () => {
        const assurance: IAssurance = { id: 123 };
        expectedResult = service.addAssuranceToCollectionIfMissing([], assurance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assurance);
      });

      it('should not add a Assurance to an array that contains it', () => {
        const assurance: IAssurance = { id: 123 };
        const assuranceCollection: IAssurance[] = [
          {
            ...assurance,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, assurance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Assurance to an array that doesn't contain it", () => {
        const assurance: IAssurance = { id: 123 };
        const assuranceCollection: IAssurance[] = [{ id: 456 }];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, assurance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assurance);
      });

      it('should add only unique Assurance to an array', () => {
        const assuranceArray: IAssurance[] = [{ id: 123 }, { id: 456 }, { id: 7749 }];
        const assuranceCollection: IAssurance[] = [{ id: 123 }];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, ...assuranceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assurance: IAssurance = { id: 123 };
        const assurance2: IAssurance = { id: 456 };
        expectedResult = service.addAssuranceToCollectionIfMissing([], assurance, assurance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assurance);
        expect(expectedResult).toContain(assurance2);
      });

      it('should accept null and undefined values', () => {
        const assurance: IAssurance = { id: 123 };
        expectedResult = service.addAssuranceToCollectionIfMissing([], null, assurance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assurance);
      });

      it('should return initial array if no Assurance is added', () => {
        const assuranceCollection: IAssurance[] = [{ id: 123 }];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, undefined, null);
        expect(expectedResult).toEqual(assuranceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
