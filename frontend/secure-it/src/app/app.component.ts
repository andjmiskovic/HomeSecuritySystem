import { Component } from '@angular/core';
import {AuthGuard} from "./model/AuthGuard";
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthGuard, AuthService]
})
export class AppComponent {
  title = 'secure-it';
}
