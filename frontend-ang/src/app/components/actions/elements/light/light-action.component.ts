import {Component} from '@angular/core';
import {BaseActionComponent} from '../../base-action.component';

@Component({
  templateUrl: './light.action.component.template.html',
  styleUrls: ['./light.action.component.style.css']
})
// TODO: cand se face subscribe, sau imediat dupa, trimite un msg sa ne dea starea initala a becului
export class LightActionComponent extends BaseActionComponent {

  isOn: boolean = true;

  buttonClick(message: string): void {
    this.wsClient.sendMessage({
      actionId: this.action.id,
      executorId: this.action.executorId,
      payload: message
    });
  }

  findLightStatus() {
    return {
      'light': true,
    }
  }

}
