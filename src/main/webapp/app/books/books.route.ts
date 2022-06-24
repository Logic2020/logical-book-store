import { Route } from '@angular/router';

import { BooksComponent } from './books.component';

export const BOOKS_ROUTE: Route = {
  path: 'books',
  component: BooksComponent,
  data: {
    pageTitle: 'books.title',
  },
};
