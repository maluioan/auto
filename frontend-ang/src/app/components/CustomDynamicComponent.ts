import {OnInit} from '@angular/core';

export interface CustomDynamicComponent extends OnInit {
  // TODO: don't use 'any'!!!
  data: any;
}
