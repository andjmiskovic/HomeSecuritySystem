import {Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {AlarmsTableDataSource} from "../../../../model/AlarmsTableDataSource";
import {DeviceManagementService} from "../../../../services/device.service";

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

  constructor(private deviceService: DeviceManagementService) {
    this.dataSource = new AlarmsTableDataSource(deviceService);
  }

  addAlarm() {

  }
}
