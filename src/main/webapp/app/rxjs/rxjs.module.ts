import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { RXJS_ROUTE } from './rxjs.route';
import { RxjsComponent } from './rxjs.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([RXJS_ROUTE])],
  declarations: [RxjsComponent],
})
export class RxjsModule {}
