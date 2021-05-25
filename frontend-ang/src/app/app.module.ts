import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule} from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';

// components
import {LoginComponent} from './components/login/login.component';
import {ChatComponent} from './components/chat/chat.component';
import {RoomComponent} from './components/room/room.component';
import {ControlPageComponent} from './components/controlpage/controlpage.component';
import {DispatcherComponent} from './components/dispatcher/dispatcher.component';
import {LoadingComponent} from './components/loading.component';

// services
import {ChatService} from './components/chat/ChatService';
import {ActionService} from './components/actions/action.service';
import {ControlPageService} from './components/controlpage/ControlPageService';
import {RoomService} from './components/room/RoomService';
import {DispatcherService} from './components/dispatcher/dispatcher.service';
import {LoginService} from './components/login/LoginService';
import {AppComponent} from './app.component';
import {MainDirective} from './directive/MainDirective';
import {HttpClientService} from './service/http-client.service';
import {ActionDirective} from './directive/ActionDirective';
import {CommonModule} from '@angular/common';
import {ErrorComponent} from './components/error/error.component';
import {FeedbackComponent} from './components/feedback/feedback.component';

export function loadData(dispatcherService: DispatcherService) {
  return () => dispatcherService.initApp();
}

@NgModule({
  declarations: [
    AppComponent,
    ControlPageComponent,
    LoginComponent,
    ChatComponent,
    RoomComponent,
    DispatcherComponent,
    LoadingComponent,
    ErrorComponent,
    FeedbackComponent,
    MainDirective,
    ActionDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
    // CommonModule
  ],
  providers: [
    LoginService,
    ChatService,
    ActionService,
    ControlPageService,
    RoomService,
    HttpClientService,
    DispatcherService,
      {
        provide: APP_INITIALIZER, useFactory: loadData, deps: [DispatcherService, HttpClientService], multi:true
      }
  ],
  bootstrap: [
    DispatcherComponent
  ]
})
export class AppModule { }
