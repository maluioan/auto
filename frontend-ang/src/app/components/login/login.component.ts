import {Component} from '@angular/core';
import {LoginService} from './LoginService';
import {LoginResult} from '../../data/LoginResult';
import {LoginData} from '../../data/LoginData';
import {CustomDynamicComponent} from '../CustomDynamicComponent';

@Component({
  selector: 'app-login',
  templateUrl: './login.template.html',
  styleUrls: ['./login.styles.css']
})
export class LoginComponent implements CustomDynamicComponent {
  // TODO: try with: import {FormBuilder} from '@angular/forms';
  // loginForm: this.fb.group({
  //   username: ['', Validators.required],
  //   password: ['', Validators.required]
  // });

  private _invalidLogin: boolean = false;
  private _invalidLoginMessage: string;
  currentStateMessage: string;
  private loginData: LoginData = {
    username: '',
    password: ''
  };

  constructor(private loginService: LoginService){}

  doLogin() {
    this.invalidLogin = false;
    this.loginService.doLogin(this.loginData, loginResult => this.onLoginFailure(loginResult));
    this.currentStateMessage = "Log in process in progress, pleaase wait";
  }

  onLoginFailure(loginResult: LoginResult): void {
    this.currentStateMessage = "Login attempt failed, please try again";
    this._invalidLoginMessage = loginResult.failureReason;
    this.invalidLogin = true;
  }

  ngOnInit(): void {
    console.log('init Login component');
    this.currentStateMessage = "Please enter you're credentials";
    // TODO: check if user is logged in
  }

  hasCurrentState(): boolean {
    return this.currentStateMessage != undefined;
  }

  /////////////////////////
  // getters and setters //
  /////////////////////////
  get invalidLogin(): boolean {
    return this._invalidLogin;
  }

  set invalidLogin(value: boolean) {
    this._invalidLogin = value;
  }

  get invalidLoginMessage(): string {
    return this._invalidLoginMessage;
  }

  get username(): string {
    return this.loginData.username;
  }

  set username(value: string) {
    this.loginData.username = value;
  }

  get password(): string {
    return this.loginData.password;
  }

  set password(value: string) {
    this.loginData.password = value;
  }

  data: object[];

}
