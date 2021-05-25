import {Injectable} from '@angular/core';
import {HttpClientService} from '../../service/http-client.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private feHttpClient: HttpClientService) {}

  getChats(userName: string) {
    this.feHttpClient.performGetChats(userName);
  }
}
