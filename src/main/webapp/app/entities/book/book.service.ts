import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBook } from '../book/book.model';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('/catalog/books');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<IBook[]>> {
    const options = createRequestOption();
    const headerOptions = new HttpHeaders().set('Content-Type', 'application/json').set('Accept', 'application/json');

    return this.http.get<IBook[]>(this.resourceUrl, {
      params: options,
      observe: 'response',
      headers: headerOptions,
    });
  }
}
