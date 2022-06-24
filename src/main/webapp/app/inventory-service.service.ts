import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';

export interface InventoryItem {
  bookId?: string;
  amount?: number;
}

@Injectable({
  providedIn: 'root',
})
export class InventoryServiceService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('/inventory');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<InventoryItem[]>> {
    const options = createRequestOption();
    const headerOptions = new HttpHeaders().set('Content-Type', 'application/json').set('Accept', 'application/json');

    return this.http.get<InventoryItem[]>(this.resourceUrl, {
      params: options,
      observe: 'response',
      headers: headerOptions,
    });
  }
}
