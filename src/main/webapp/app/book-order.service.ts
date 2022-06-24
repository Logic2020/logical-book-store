import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';

export interface OrderItem {
  bookId?: string;
  quantity?: number;
}

export interface Order {
  items?: OrderItem[];
}

@Injectable({
  providedIn: 'root',
})
export class BookOrderService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('/orders');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  orderBooks(books: Order): Observable<HttpResponse<any>> {
    const options = createRequestOption();
    const headerOptions = new HttpHeaders().set('Content-Type', 'application/json').set('Accept', 'application/json');

    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.http.post<any>(this.resourceUrl, books, {
      params: options,
      observe: 'response',
      headers: headerOptions,
    });
  }
}
