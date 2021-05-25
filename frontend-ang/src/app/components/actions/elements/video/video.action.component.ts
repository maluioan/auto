import {Component} from '@angular/core';
import {BaseActionComponent} from '../../base-action.component';
import {WsClient} from '../../../../service/ws.client';

@Component({
  templateUrl: './video.action.component.template.html',
  styleUrls: ['./video.action.component.style.css']
})
export class VideoActionComponent extends BaseActionComponent{

  set wsClient(value: WsClient) {
    console.log("Maybe for video component we should create another connection");
  }
}
