import {Component, ComponentFactoryResolver, Injectable, Input, OnInit, ViewChild} from '@angular/core';
import {MainDirective} from '../../directive/MainDirective';
import {StateDate} from '../../data/StateDate';
import {CustomDynamicComponent} from '../CustomDynamicComponent';
import {DispatcherService} from './dispatcher.service';


// TODO: bundle FE and BE in one sngle file:
// https://medium.com/bb-tutorials-and-thoughts/how-to-build-angular-with-java-backend-for-production-9cc04f97e3c


@Component({
 selector: 'dispatcher',
  template:'<div class="app-container">' +
    '<ng-template mainDisplay></ng-template>' +
    '</div>',
  styleUrls: ['../../common/common.css']
})
@Injectable({
  providedIn: 'root'
})
export class DispatcherComponent implements OnInit {
  @ViewChild(MainDirective, {static: true})
  mainDisplay: MainDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private dispatcherService: DispatcherService) {}

  ngOnInit(): void {
    this.dispatcherService.dispatcherComponent = this;
    this.dispatcherService.showStatusDisplay();
  }

  setState(stateData: StateDate): void {
    this.showComponent(stateData);
  }

  private showComponent(stateData: StateDate): void {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(stateData.componentToRender);

    const viewContainerRef = this.mainDisplay.viewContainerRef;
    viewContainerRef.clear();

    const componentRef = viewContainerRef.createComponent<CustomDynamicComponent>(componentFactory);
    if  (stateData.data) {
      componentRef.instance.data = stateData.data;
    }
  }
}
