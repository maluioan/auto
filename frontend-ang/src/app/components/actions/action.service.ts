import {Injectable} from '@angular/core';
import {ControlPageService} from '../controlpage/ControlPageService';
import {HttpClientService} from '../../service/http-client.service';

@Injectable({
  providedIn: 'root'
})
// Deprecated
export class ActionService {

  constructor(private feHttpClientService: HttpClientService) {}


}
