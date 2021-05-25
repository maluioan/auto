import {Injectable, Input, OnInit} from '@angular/core';
import {LoginComponent} from '../login/login.component';
import {StateDate} from '../../data/StateDate';
import {DispatcherComponent} from './dispatcher.component';
import {ControlPageComponent} from '../controlpage/controlpage.component';
import {AppStats} from '../../data/AppStats';
import {HttpClientService} from '../../service/http-client.service';
import {LoadingComponent} from '../loading.component';
import {ErrorComponent} from '../error/error.component';

@Injectable({
  providedIn: 'root'
})
export class DispatcherService {

  private _dispatcherComponent: DispatcherComponent;

  // TODO: try to init this immediately after first rendering
  @Input() status: AppStats;

  constructor(private feHttpClientService: HttpClientService){}

  initApp(): void {
    this.feHttpClientService.performGetStats().subscribe(
      appStats => {
        this.status = appStats;
        this.showStatusDisplay();
      },
      appStats => {
        this.status = {
          error: true,
          loggedIn: false,
        };
        this.showStatusDisplay();
      },
      () => console.log('stats request completeted')
    );
  }

  loginSuccess(appStats: AppStats): void {
    this.status = appStats;

    this.changeState({
      componentToRender: ControlPageComponent,
      data: this.status
    });
  }

  showStatusDisplay() {
    // TODO: create a factory
    let stateData: StateDate;

    if (this.status === undefined) {
      stateData = {componentToRender: LoadingComponent, data: ''};

    } else if (this.status.error == true) {
      stateData = {componentToRender: ErrorComponent, data: ''};

    } else if (this.status.loggedIn == true){
      stateData = {componentToRender: ControlPageComponent, data: this.status};

    } else {
      stateData = {componentToRender: LoginComponent, data: ''};
    }
    this.changeState(stateData);
  }

  changeState(stateDate: StateDate): void {
    console.log('dispatche change state');
    this._dispatcherComponent.setState(stateDate);
  }

  ///////////////////
  // gett & setter //
  ///////////////////
  get dispatcherComponent(): DispatcherComponent {
    return this._dispatcherComponent;
  }

  set dispatcherComponent(value: DispatcherComponent) {
    this._dispatcherComponent = value;
  }
}

// TODO: try 'APP_INITIALIZER'

// TODO: cand si cum are rost sa avem ngOnInit intr-un serviciu??
// ngOnInit(): void {
//   console.log('disp service');
// }



// TODO: try with routers:   https://www.baeldung.com/spring-boot-angular-web:  routes, form control etc
// @Component({
//   selector: 'app-user-form',
//   templateUrl: './user-form.component.html',
//   styleUrls: ['./user-form.component.css']
// })
// export class UserFormComponent {
//
//   user: User;
//
//   constructor(
//     private route: ActivatedRoute,
//     private router: Router,
//     private userService: UserService) {
//     this.user = new User();
//   }
//
//   onSubmit() {
//     this.userService.save(this.user).subscribe(result => this.gotoUserList());
//   }
//
//   gotoUserList() {
//     this.router.navigate(['/users']);     TODO: this might be usefull
//   }
// }
