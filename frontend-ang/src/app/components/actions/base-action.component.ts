import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActionService} from './action.service';
import {WsClient} from '../../service/ws.client';
import {ActionData} from '../../data/ActionData';

@Component({
  // selector: 'action-component',
  template: 'ar trebui s fie doar o superclasa pt restul, cum declar asa ceva?',
  // styleUrls: ['./base.action.template.css']
})
// TODO: add onDestory, cand se distuge, se unsubscribe !!!
export abstract class BaseActionComponent implements OnInit, OnDestroy {

  private _userName: string;
  private _wsClient: WsClient;
  private  _action: ActionData;
  private subscribed: boolean = false;

  constructor(private actionService: ActionService) {}

  // START: lifecycle hooks
  ngOnInit(): void {
    console.log('init BaseActionComponent: ' + this._action.name);
    if (this.wsClient) {
      this.subscribeToAction()
    }
  }

  ngOnDestroy(): void {
    if (this._wsClient) {
      this._wsClient.unsubscribeFrom(this._action.id);
    }
  }
  // END: lifecycle hooks

  private subscribeToAction(): void {
    // TODO: refactor, use observables, takeWhile maybe?
    this.subscribe();
    if (this.subscribed === false) {
      setTimeout(() => this.subscribe(), 1000);
    }
  }

  subscribe() {
    if (this.subscribed === false) {
      let obj = this._wsClient.subscribeToTopic('/action/' + this._action.id, (msg) => {
        console.log('hello bai: ' + msg);
      });
      console.log("subscr obj = " + obj);
      this.subscribed = true;
    }
  }
  //
  actionSubscribed() {
    let connected = this._wsClient.isConnected();
    return {
      connected: connected,
      disconnected: !connected
    }
  }

  /////////////////////
  // getters & setters
  /////////////////////

  get userName(): string {
    return this._userName;
  }

  set userName(value: string) {
    this._userName = value;
  }

  get wsClient(): WsClient {
    return this._wsClient;
  }

  set wsClient(value: WsClient) {
    this._wsClient = value;
  }

  get action(): ActionData {
    return this._action;
  }

  set action(value: ActionData) {
    this._action = value;
  }

  data: any;

}
