import { TestBed } from '@angular/core/testing';

import { InventoryServiceService } from './inventory-service.service';

describe('InventoryServiceService', () => {
  let service: InventoryServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoryServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
