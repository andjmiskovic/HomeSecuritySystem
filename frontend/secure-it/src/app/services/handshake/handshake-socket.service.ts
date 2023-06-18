import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {HandshakeWebsocketShareService} from "./handshake-websocketshare.service";
import {AuthService} from "../auth.service";
import {User} from "../../model/User";
import {environment} from "../../environment.development";

@Injectable({
  providedIn: 'root'
})
export class HandshakeWebSocketAPI {
  webSocketEndPoint: string = environment.apiUrl + "/ws";
  topic: string = "/user/queue/devices"
  stompClient: any;
  loggedUser: User | undefined;

  constructor(private websocketShare: HandshakeWebsocketShareService, private authService: AuthService) {
    authService.getCurrentlyLoggedUser().subscribe((user) => {
      console.log(user)

      if (user && user.email != this.loggedUser?.email) {
        console.log(user);
        this.loggedUser = user;
        this.connect();
      }
      else {
        this.loggedUser = undefined;
        this.disconnect();
      }
    })
  }

  connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    console.log("Initialize WebSocket Connection 2");

    this.stompClient = Stomp.over(ws);
    this.stompClient.reconnect_delay = 1000;
    this.stompClient.connect({}, (frame: any) => {
      this.stompClient.subscribe(this.topic, (sdkEvent: any) => {
        console.log('Received message:', JSON.parse(sdkEvent.body));
        this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, () => this.errorCallBack);
  };

  disconnect() {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  errorCallBack(error: any) {
    alert("Error: " + error);
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this.connect();
    }, 5000);
  }

  onMessageReceived(message: any) {
    this.websocketShare.onNewValueReceive(message.body);
  }

  send(message: any) {
    console.log("OVDE ALOOO MAJKE MI");

    this.stompClient.send()
  }
}
