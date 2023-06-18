import {Component, Input, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {AlarmsTableDataSource} from "../../../../model/AlarmsTableDataSource";
import {DeviceManagementService} from "../../../../services/device.service";
import {DeviceDetailsResponse} from "../../../../model/Device";
import {EditAlarmsDialogComponent} from "../edit-alarms-dialog/edit-alarms-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-alarms-container',
  templateUrl: './alarms-container.component.html',
  styleUrls: ['./alarms-container.component.css']
})
export class AlarmsContainerComponent {
  displayedColumns = ["message", "time", "deviceId"];
  dataSource: AlarmsTableDataSource;
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  @Input() device: DeviceDetailsResponse = new DeviceDetailsResponse();

  constructor(private deviceService: DeviceManagementService, private dialog: MatDialog) {
    this.dataSource = new AlarmsTableDataSource(deviceService);
  }

  ngOnInit() {
    this.loadAlarms(this.device);
  }

  loadAlarms(device: DeviceDetailsResponse) {
    console.log("OPAA")
    console.log(device)
    if (device.id) {
      console.log("stigao sam")
      this.dataSource.loadAlarms(device.id);
    }
  }

  editAlarms() {
    const dialogRef = this.dialog.open(EditAlarmsDialogComponent, {
      height: '600px',
      width: '1000px'
    });
    dialogRef.componentInstance.device = this.device;
  }
}
