import {Component, OnDestroy} from '@angular/core';
import {DispatcherService} from '../dispatcher/dispatcher.service';
import {CustomDynamicComponent} from '../CustomDynamicComponent';
import {WsClient} from '../../service/ws.client';
import {RoomService} from '../room/RoomService';
import {RoomActionData} from '../../data/RoomActionData';
import {ActionData} from '../../data/ActionData';

@Component({
  selector: 'control-page',
  templateUrl: './controlpage.template.html',
  styleUrls: ['./control.page.style.css']
})
export class ControlPageComponent implements CustomDynamicComponent, OnDestroy {
  private _wsClient: WsClient;
  private _roomActions: RoomActionData[] = [];
  connected: boolean = false;

  constructor(private dispatcherService: DispatcherService,
              private roomService: RoomService) {}

  ngOnInit(): void {
    this._wsClient = new WsClient(this.token,
      (successFrame) => this.loadRooms(),
      (errorFrame) => console.log('Ws connection failed!'));
  }

  ngOnDestroy(): void {
    this.wsClient.disconnect();
  }

  private loadRooms(): void {
    this.roomService.getRoomsForUser(this.userName,
      roomActions => this.onSuccess(roomActions),
      failureMessage => console.log('Failed to get rooms')
    );

  }

  private onSuccess(roomActions: RoomActionData[]): void {
    this._roomActions = roomActions;
    this.connected = true;
  }

  // getters and setters
  get wsClient(): WsClient {
    return this._wsClient;
  }

  get roomActions(): RoomActionData[] {
    return this._roomActions;
  }


  get userName():string {
    let status = this.dispatcherService.status;
    return status != null ? status.userName : '';
  }

  get token():string {
    let status = this.dispatcherService.status;
    return status != null ? status.token : '';
  }

  get actions(): ActionData[] {
    let onlyActions:ActionData[] = new Array();
    this.roomActions.map(roomAction => {
      roomAction.actions.forEach(action => {
        onlyActions.push(action);
      })
    });
    return onlyActions;
  }

  data: any;
}
