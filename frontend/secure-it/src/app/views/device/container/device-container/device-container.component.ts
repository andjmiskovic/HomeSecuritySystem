import {Component, OnInit} from '@angular/core';
import {DeviceDetailsResponse} from "../../../../model/Device";
import {ActivatedRoute} from "@angular/router";
import {DeviceManagementService} from "../../../../services/device.service";

@Component({
  selector: 'app-device-container',
  templateUrl: './device-container.component.html',
  styleUrls: ['./device-container.component.css']
})
export class DeviceContainerComponent implements OnInit {
  device = new DeviceDetailsResponse();
  deviceId: string | null = "";

  constructor(private router: ActivatedRoute, private deviceService: DeviceManagementService) {

  }

  ngOnInit(): void {
    this.deviceId = this.router.snapshot.paramMap.get('id');
    this.deviceService.getDevice(this.deviceId).subscribe({
      next: value => this.device = value
    })
  }
}
