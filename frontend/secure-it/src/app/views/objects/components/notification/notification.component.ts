import {Component, Input, OnInit} from '@angular/core';
import {HandshakeWebsocketShareService} from "../../../../services/handshake/handshake-websocketshare.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DeviceHandshakeData} from "../../../../model/Device";
import {DeviceManagementService} from "../../../../services/device.service";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent {

  @Input() device: DeviceHandshakeData | undefined = undefined;
  @Input() code: string | undefined = undefined;

  constructor(private deviceManagementService: DeviceManagementService) {
  }

  handshakeDevice() {
    this.deviceManagementService.handshakeWeb(this.code!).subscribe({
      next: (res) => console.log(res),
      error: (err) => console.error(err)
    })
  }
}
