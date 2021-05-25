import {Component, ComponentFactoryResolver, Input, Type, ViewChild} from '@angular/core';
import {CustomDynamicComponent} from '../CustomDynamicComponent';
import {RoomActionData} from '../../data/RoomActionData';
import {WsClient} from '../../service/ws.client';
import {ActionDirective} from '../../directive/ActionDirective';
import {ActionData} from '../../data/ActionData';
import {BaseActionComponent} from '../actions/base-action.component';
import {UnknownActionComponent} from '../actions/elements/unknown/unknown-action.component';
import {LightActionComponent} from '../actions/elements/light/light-action.component';
import {VideoActionComponent} from '../actions/elements/video/video.action.component';


@Component({
  selector: 'room-component',
  templateUrl: './roomcomponent.template.html',
  styleUrls: ['./roomcomponent.template.css']
})
export class RoomComponent implements CustomDynamicComponent {
  @ViewChild(ActionDirective, {static: true})
  private actionDirective: ActionDirective;

  @Input()
  userName: string;
  @Input()
  wsClient: WsClient;
  @Input()
  roomActionData: RoomActionData;

  private typeToComponentMap: Map<string, Type<BaseActionComponent>> = new Map<string, Type<BaseActionComponent>>();

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
    this.typeToComponentMap.set('LIGHT', LightActionComponent);
    this.typeToComponentMap.set('VIDEO', VideoActionComponent);
  }

  ngOnInit(): void {
     this.roomActionData.actions.forEach((actionData) => {
       this.createDynamicActionComponent(actionData)
     });
  }

  private createDynamicActionComponent(action: ActionData): void {
    let componentType = this.typeToComponentMap.get(action.actionType);
    componentType = componentType ? componentType : UnknownActionComponent;

    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentType);
    const viewContainerRef = this.actionDirective.viewContainerRef;

    const componentRef = viewContainerRef.createComponent<BaseActionComponent>(componentFactory);
    componentRef.instance.userName = this.userName;
    componentRef.instance.wsClient = this.wsClient;
    componentRef.instance.action = action;
  }

  // dynamic classes

  // TODO: move this from here

  // getters, setters
  // get roomActions(): RoomActionData[] {
  //   return this._roomActions;
  // }

  data: object[];
}
