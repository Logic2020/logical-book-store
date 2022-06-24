import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { BOOKS_ROUTE } from './books.route';
import { BooksComponent } from './books.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([BOOKS_ROUTE])],
  declarations: [BooksComponent],
})
export class BooksModule {}
