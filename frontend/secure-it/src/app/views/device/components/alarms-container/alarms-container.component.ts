import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {AlarmsTableDataSource} from "../../../../model/AlarmsTableDataSource";
import {DeviceManagementService} from "../../../../services/device.service";
import {DeviceDetailsResponse} from "../../../../model/Device";
import {EditAlarmsDialogComponent} from "../edit-alarms-dialog/edit-alarms-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {HandshakeWebsocketShareService} from "../../../../services/handshake/handshake-websocketshare.service";
import {Subscription} from "rxjs";
import {NotificationComponent} from "../../../objects/components/notification/notification.component";
import {HttpErrorResponse} from "@angular/common/http";
import {AlarmWebsocketShareService} from "../../../../services/alarm/alarm.websocketshare.service";

@Component({
  selector: 'app-alarms-container',
  templateUrl: './alarms-container.component.html',
  styleUrls: ['./alarms-container.component.css']
})
export class AlarmsContainerComponent implements OnInit, OnDestroy{
  displayedColumns = ["message", "time", "deviceId"];
  dataSource: AlarmsTableDataSource;
  socketSubscription: Subscription | undefined;
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;
  @Input() device: DeviceDetailsResponse = new DeviceDetailsResponse();

  constructor(private deviceService: DeviceManagementService, private dialog: MatDialog,
              private websocketService: AlarmWebsocketShareService) {
    this.dataSource = new AlarmsTableDataSource(deviceService);
  }

  ngOnInit() {
    this.loadAlarms(this.device);
    this.subscribeToNewNotifications();
  }


  private subscribeToNewNotifications(): void {
    this.socketSubscription = this.websocketService.getNewValue().subscribe({
      next: (res: any) => {
        console.log(res)
        if (res) {
          let body = JSON.parse(res);
          this.dataSource.loadAlarms(body.deviceId);
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }


  ngOnDestroy(): void {
    this.socketSubscription?.unsubscribe();
    this.websocketService.onNewValueReceive('');
  }

  loadAlarms(device: DeviceDetailsResponse) {
    if (device.id) {
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
