import {Component} from '@angular/core';

@Component({
  selector: 'error-component',
  template: '<div class="error">{{errorMessage}}</div>',
  styleUrls: ['../../common/common.css']
})
export class ErrorComponent {
  errorMessage: string = "Error loading app, pleasse refresh page";
}
