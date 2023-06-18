import {Component} from '@angular/core';
import {AuthGuard} from "./model/AuthGuard";
import {AuthService} from "./services/auth.service";
import {HandshakeWebSocketAPI} from "./services/handshake/handshake-socket.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthGuard, AuthService]
})
export class AppComponent {
  title = 'secure-it';

  constructor(private handshakeWebSocketAPI: HandshakeWebSocketAPI) {

  }

}
