import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[mainDisplay]'
})
export class MainDirective {
  constructor(public viewContainerRef: ViewContainerRef) {}
}
