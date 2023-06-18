import {Component, Input} from '@angular/core';
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
