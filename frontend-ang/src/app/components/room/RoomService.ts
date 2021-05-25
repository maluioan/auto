import {Injectable} from '@angular/core';
import {ControlPageService} from '../controlpage/ControlPageService';
import {HttpClientService} from '../../service/http-client.service';
import {RoomActionData} from '../../data/RoomActionData';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(private feHttpClient: HttpClientService) {}

  getRoomsForUser(userName: string, onSuccess, onFailure) {
    this.feHttpClient.performGetRoomsForUser(userName).subscribe(
      successResponse => onSuccess(successResponse),
        failureResponse => onFailure(failureResponse)
    );
  }
}
