import Stomp from "stompjs";
import SockJS from 'sockjs-client';

// TODO: treat session expire cases
// TODO: browser close cases
// TODO: BE down/restart cases, si modulul de 'storefront' si cel de 'dispatcher'
// TODO: all cases of exceptions, make sure to unsubscribe/disconnect
// TODO: este un heartbeat, oare nu ne putem baza pe ala, cev disconnect handler??
export class WsClient {
  private endpointURL = 'http://localhost:8004/endpoint/control';
  stompClient: any;

  constructor(private token: string, connectionCallback, connectionFailedCallback) {
    console.log("created ws client with token: " + token);
    this.connect(token, connectionCallback, connectionFailedCallback);
  }

  private connect(token: string, connectionCallback, connectionFailedCallback): void {
    var wsSockJs = new SockJS(this.endpointURL + "?dtk=" + token);
    this.stompClient = Stomp.over(wsSockJs);
    // this.som
    const connectResult = this.stompClient.connect({},
      function(successFrame) {
        console.log(wsSockJs); // TODO: get ses id din _transport.url sau din alta parte
        connectionCallback(successFrame);
      },
      function(errorFrame) {
        connectionFailedCallback(errorFrame);
      });

    console.log(connectResult);
  }

  public subscribeToTopic(subsId: string, callback): boolean {
    // TODO: add rxJs observable/subject
    if (!this.isConnected()) {
      console.log('Stomp client not connected!');
      return undefined;
    }
    return this.stompClient.subscribe('/dispatcher-broker/topic' + subsId, callback, {id: subsId});
  }

  public subscribeToPrivate(subsId: string, callback): boolean {
    // TODO: add rxJs observable/subject
    if (!this.isConnected()) {
      console.log('Stomp client not connected!');
      return undefined;
    }
    return this.stompClient.subscribe('/dispatcher-broker/topic' + subsId, callback, {id: subsId});
  }

  public unsubscribeFrom(subsId: string): void {
    this.stompClient.unsubscribe(subsId);
  }

  public disconnect(): void {
    console.log('disconnect'); // TODO: implement disconnect
    this.stompClient.disconnect();
  }

  sendMessage(message: object) {
    this.stompClient.send('/app/client/message', {}, JSON.stringify(message));
  }

  isConnected(): boolean {
    return this.stompClient.connected;
  }
}
