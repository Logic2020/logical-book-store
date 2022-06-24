/* eslint-disable @angular-eslint/no-empty-lifecycle-method */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/member-ordering */
import { Component, OnInit } from '@angular/core';
import { fromEvent, interval, merge, noop, NEVER, Observable } from 'rxjs';
import { map, mapTo, scan, startWith, switchMap, tap } from 'rxjs/operators';

interface State {
  count: boolean;
  countup: boolean;
  speed: number;
  value: number;
  increase: number;
}

@Component({
  selector: 'logic-rxjs',
  templateUrl: './rxjs.component.html',
  styleUrls: ['./rxjs.component.scss'],
})
export class RxjsComponent implements OnInit {
  getElem = (id: string): HTMLElement => document.getElementById(id) as HTMLElement;
  getVal = (id: string): number => {
    const el = this.getElem(id);
    if (el instanceof HTMLInputElement) {
      return parseInt(el['value'], 10);
    }
    return 0;
  };
  fromClick = (id: string): Observable<Event> => fromEvent(this.getElem(id), 'click');
  fromClickAndMapTo = (id: string, obj: {}): Observable<{}> => this.fromClick(id).pipe(mapTo(obj));
  fromClickAndMap = (id: string, fn: any): Observable<{}> => this.fromClick(id).pipe(map(fn));
  setValue = (val: number): string => (this.getElem('counter').innerText = val.toString());

  events$ = merge(
    this.fromClickAndMapTo('start', { count: true }),
    this.fromClickAndMapTo('pause', { count: false }),
    this.fromClickAndMapTo('reset', { value: 0 }),
    this.fromClickAndMapTo('countup', { countup: true }),
    this.fromClickAndMapTo('countdown', { countup: false }),
    this.fromClickAndMap('setto', (_: any) => ({ value: this.getVal('value') })),
    this.fromClickAndMap('setspeed', (_: any) => ({ speed: this.getVal('speed') })),
    this.fromClickAndMap('setincrease', (_: any) => ({ increase: this.getVal('increase') }))
  );

  ngOnInit(): void {
    /* const stopWatch$ = this.events$.pipe(
      startWith({
        count: false,
        speed: 1000,
        value: 0,
        countup: true,
        increase: 1,
      }),
      scan((state: State, curr): State => ({ ...state, ...curr }), {}),
      tap((state: State) => this.setValue(state.value)),
      switchMap((state: State) =>
        state.count
          ? interval(state.speed).pipe(
              tap(_ => (state.value += state.countup ? state.increase : -state.increase)),
              tap(_ => this.setValue(state.value))
            )
          : NEVER
      )
    );

    stopWatch$.subscribe(); */
  }
}
