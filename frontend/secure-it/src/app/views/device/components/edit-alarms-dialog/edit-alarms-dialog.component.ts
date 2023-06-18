import {Component, Input, OnInit} from '@angular/core';
import {DeviceManagementService} from "../../../../services/device.service";
import {DeviceDetailsResponse} from "../../../../model/Device";

@Component({
  selector: 'app-edit-alarms-dialog',
  templateUrl: './edit-alarms-dialog.component.html',
  styleUrls: ['./edit-alarms-dialog.component.css']
})
export class EditAlarmsDialogComponent implements OnInit {
  @Input() device: DeviceDetailsResponse = new DeviceDetailsResponse();

  constructor(private deviceService: DeviceManagementService) {
  }

  ngOnInit() {
  }

  removeAlarm(index: number) {
    this.device.alarms.splice(index);
  }

  addNewAlarm() {
    this.device.alarms.push(["", "", ""]);
  }

  saveChanges() {
    // call endpoint
  }
}
