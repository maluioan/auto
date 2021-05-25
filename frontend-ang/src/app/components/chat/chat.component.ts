import {Component, Input, OnInit} from '@angular/core';
import {ChatService} from './ChatService';
import {CustomDynamicComponent} from '../CustomDynamicComponent';
import {WsClient} from '../../service/ws.client';

@Component({
  selector: 'chat-component',
  templateUrl: './chat.template.html'
})
export class ChatComponent implements CustomDynamicComponent {

  @Input()
  userName: string;
  @Input()
  wsClient: WsClient;

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    console.log('init ChatComponent');
    this.chatService.getChats(this.userName);
  }

  data: object[];

}
