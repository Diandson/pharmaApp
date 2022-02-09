import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TypeStructure } from 'app/entities/enumerations/type-structure.model';
import { IStructure, Structure } from '../structure.model';

import { StructureService } from './structure.service';

describe('Structure Service', () => {
  let service: StructureService;
  let httpMock: HttpTestingController;
  let elemDefault: IStructure;
  let expectedResult: IStructure | IStructure[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StructureService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      denomination: 'AAAAAAA',
      ifu: 'AAAAAAA',
      rccm: 'AAAAAAA',
      codePostal: 'AAAAAAA',
      localisation: 'AAAAAAA',
      contact: 'AAAAAAA',
      regime: 'AAAAAAA',
      division: 'AAAAAAA',
      email: 'AAAAAAA',
      logoContentType: 'image/png',
      logo: 'AAAAAAA',
      cachetContentType: 'image/png',
      cachet: 'AAAAAAA',
      signatureContentType: 'image/png',
      signature: 'AAAAAAA',
      dateConfig: currentDate,
      pdg: 'AAAAAAA',
      type: TypeStructure.SIEGE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateConfig: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateConfig: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateConfig: currentDate,
        },
        returnedFromService
      );

      service.create(new Structure()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          ifu: 'BBBBBB',
          rccm: 'BBBBBB',
          codePostal: 'BBBBBB',
          localisation: 'BBBBBB',
          contact: 'BBBBBB',
          regime: 'BBBBBB',
          division: 'BBBBBB',
          email: 'BBBBBB',
          logo: 'BBBBBB',
          cachet: 'BBBBBB',
          signature: 'BBBBBB',
          dateConfig: currentDate.format(DATE_TIME_FORMAT),
          pdg: 'BBBBBB',
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateConfig: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Structure', () => {
      const patchObject = Object.assign(
        {
          rccm: 'BBBBBB',
          codePostal: 'BBBBBB',
          contact: 'BBBBBB',
          regime: 'BBBBBB',
          division: 'BBBBBB',
          email: 'BBBBBB',
          logo: 'BBBBBB',
          signature: 'BBBBBB',
          dateConfig: currentDate.format(DATE_TIME_FORMAT),
          pdg: 'BBBBBB',
          type: 'BBBBBB',
        },
        new Structure()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateConfig: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Structure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          ifu: 'BBBBBB',
          rccm: 'BBBBBB',
          codePostal: 'BBBBBB',
          localisation: 'BBBBBB',
          contact: 'BBBBBB',
          regime: 'BBBBBB',
          division: 'BBBBBB',
          email: 'BBBBBB',
          logo: 'BBBBBB',
          cachet: 'BBBBBB',
          signature: 'BBBBBB',
          dateConfig: currentDate.format(DATE_TIME_FORMAT),
          pdg: 'BBBBBB',
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateConfig: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Structure', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStructureToCollectionIfMissing', () => {
      it('should add a Structure to an empty array', () => {
        const structure: IStructure = { id: 123 };
        expectedResult = service.addStructureToCollectionIfMissing([], structure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(structure);
      });

      it('should not add a Structure to an array that contains it', () => {
        const structure: IStructure = { id: 123 };
        const structureCollection: IStructure[] = [
          {
            ...structure,
          },
          { id: 456 },
        ];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, structure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Structure to an array that doesn't contain it", () => {
        const structure: IStructure = { id: 123 };
        const structureCollection: IStructure[] = [{ id: 456 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, structure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(structure);
      });

      it('should add only unique Structure to an array', () => {
        const structureArray: IStructure[] = [{ id: 123 }, { id: 456 }, { id: 94117 }];
        const structureCollection: IStructure[] = [{ id: 123 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, ...structureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const structure: IStructure = { id: 123 };
        const structure2: IStructure = { id: 456 };
        expectedResult = service.addStructureToCollectionIfMissing([], structure, structure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(structure);
        expect(expectedResult).toContain(structure2);
      });

      it('should accept null and undefined values', () => {
        const structure: IStructure = { id: 123 };
        expectedResult = service.addStructureToCollectionIfMissing([], null, structure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(structure);
      });

      it('should return initial array if no Structure is added', () => {
        const structureCollection: IStructure[] = [{ id: 123 }];
        expectedResult = service.addStructureToCollectionIfMissing(structureCollection, undefined, null);
        expect(expectedResult).toEqual(structureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
