import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {LoginData} from '../data/LoginData';
import {LoginResult} from '../data/LoginResult';
import {Observable} from 'rxjs';
import {AppStats} from '../data/AppStats';
import {RoomActionData} from '../data/RoomActionData';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  // TODO: add in env file
  private loginUrl: string = '/api/login_check';
  private statusUrl: string = '/api/stats';
  private roomsUrl: string = '/api/rooms';

  constructor(private http: HttpClient) {}

  performLogin(loginData: LoginData): Observable<LoginResult> {
    const fd = new FormData();
    fd.append('util', loginData.username);
    fd.append('par', loginData.password);

    return this.http.post<LoginResult>(this.loginUrl, fd);
  }

  performGetStats(): Observable<AppStats> {
    return this.http.get<AppStats>(this.statusUrl);
  }

  performGetRoomsForUser(userName: string): Observable<RoomActionData[]> {
    const options = {
      params: new HttpParams().set('un',  userName).set('count', "10"),
    };

    return this.http.get<RoomActionData[]>(this.roomsUrl, options);
  }

  performGetChats(userName: string) {

  }
}
