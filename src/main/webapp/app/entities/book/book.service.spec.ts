import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Book, IBook } from './book.model';

import { BookService } from './book.service';

describe('User Service', () => {
  let service: BookService;
  let httpMock: HttpTestingController;
  let expectedResult: IBook | IBook[] | boolean | number | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should return Users', () => {
      service.query().subscribe(received => {
        expectedResult = received.body;
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([new Book('1001', 'Fake Book', ['Fake Author'], 2022, 10.0)]);
      expect(expectedResult).toEqual([{ id: '1001', title: 'Fake Book' }]);
    });

    it('should propagate not found response', () => {
      service.query().subscribe({
        error: (error: HttpErrorResponse) => (expectedResult = error.status),
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush('Internal Server Error', {
        status: 500,
        statusText: 'Inernal Server Error',
      });
      expect(expectedResult).toEqual(500);
    });
  });
});
