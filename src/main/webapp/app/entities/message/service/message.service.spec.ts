import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMessage, Message } from '../message.model';

import { MessageService } from './message.service';

describe('Message Service', () => {
  let service: MessageService;
  let httpMock: HttpTestingController;
  let elemDefault: IMessage;
  let expectedResult: IMessage | IMessage[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MessageService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      message: 'AAAAAAA',
      dateMessage: currentDate,
      to: 'AAAAAAA',
      vue: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateMessage: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Message', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateMessage: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMessage: currentDate,
        },
        returnedFromService
      );

      service.create(new Message()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Message', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          message: 'BBBBBB',
          dateMessage: currentDate.format(DATE_TIME_FORMAT),
          to: 'BBBBBB',
          vue: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMessage: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Message', () => {
      const patchObject = Object.assign(
        {
          to: 'BBBBBB',
          vue: true,
        },
        new Message()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateMessage: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Message', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          message: 'BBBBBB',
          dateMessage: currentDate.format(DATE_TIME_FORMAT),
          to: 'BBBBBB',
          vue: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMessage: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Message', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMessageToCollectionIfMissing', () => {
      it('should add a Message to an empty array', () => {
        const message: IMessage = { id: 123 };
        expectedResult = service.addMessageToCollectionIfMissing([], message);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(message);
      });

      it('should not add a Message to an array that contains it', () => {
        const message: IMessage = { id: 123 };
        const messageCollection: IMessage[] = [
          {
            ...message,
          },
          { id: 456 },
        ];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, message);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Message to an array that doesn't contain it", () => {
        const message: IMessage = { id: 123 };
        const messageCollection: IMessage[] = [{ id: 456 }];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, message);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(message);
      });

      it('should add only unique Message to an array', () => {
        const messageArray: IMessage[] = [{ id: 123 }, { id: 456 }, { id: 36118 }];
        const messageCollection: IMessage[] = [{ id: 123 }];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, ...messageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const message: IMessage = { id: 123 };
        const message2: IMessage = { id: 456 };
        expectedResult = service.addMessageToCollectionIfMissing([], message, message2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(message);
        expect(expectedResult).toContain(message2);
      });

      it('should accept null and undefined values', () => {
        const message: IMessage = { id: 123 };
        expectedResult = service.addMessageToCollectionIfMissing([], null, message, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(message);
      });

      it('should return initial array if no Message is added', () => {
        const messageCollection: IMessage[] = [{ id: 123 }];
        expectedResult = service.addMessageToCollectionIfMissing(messageCollection, undefined, null);
        expect(expectedResult).toEqual(messageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
