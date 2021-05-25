import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  // templateUrl: './app.component.html',
  template: '<div>' +
    '<h3>Main App component</h3>' +
    '<control-page></control-page>' +
    '</div>',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-ang';
}
