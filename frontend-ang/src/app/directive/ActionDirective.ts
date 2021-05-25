import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[action-directive]'
})
export class ActionDirective {
  constructor(public viewContainerRef: ViewContainerRef){}
}
