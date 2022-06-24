import { TestBed } from '@angular/core/testing';

import { BookOrderService } from './book-order.service';

describe('BookOrderService', () => {
  let service: BookOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookOrderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
