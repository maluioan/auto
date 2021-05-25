import {Injectable} from '@angular/core';
import {LoginResult} from '../../data/LoginResult';
import {LoginData} from '../../data/LoginData';
import {DispatcherService} from '../dispatcher/dispatcher.service';
import {HttpClientService} from '../../service/http-client.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private dispatcherService: DispatcherService,
              private feHttpClientService: HttpClientService) {}

  doLogin(loginData: LoginData, onLoginError): void {
    this.feHttpClientService.performLogin(loginData).subscribe(
      logResult => this.onLoginSuccess(logResult, onLoginError),
      logResult => onLoginError(logResult.error)
    );
  }

  onLoginSuccess(logResult: LoginResult, onLoginError): void {
    // TODO: use rxJS
    this.feHttpClientService.performGetStats().subscribe(
      appStats => this.dispatcherService.loginSuccess(appStats),
      appStats => onLoginError({
        success: false,
        failureReason: 'unexpected reason'
      })
    );
  }
}
