import { Route } from '@angular/router';

import { RxjsComponent } from './rxjs.component';

export const RXJS_ROUTE: Route = {
  path: 'rxjs',
  component: RxjsComponent,
  data: {
    pageTitle: 'rxjs',
  },
};
