/* eslint-disable @angular-eslint/no-empty-lifecycle-method */
/* eslint-disable @typescript-eslint/no-empty-function */
/* eslint-disable no-console */
import { Component, AfterContentInit, ContentChild, ElementRef, AfterViewInit, ViewChild } from '@angular/core';

@Component({
  selector: 'logic-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent implements AfterContentInit, AfterViewInit {
  myContext = { $implicit: 'World', localSk: 'Svet', sweet: 'sugar' };
  projectedContext = { $implicit: 'Projected', localSk: 'Svet', sour: 'lemons' };

  @ViewChild('intheview') viewcontent!: ElementRef;
  @ContentChild('findmenow') projectedcontent!: ElementRef;

  ngAfterContentInit(): void {}

  ngAfterViewInit(): void {}
}
