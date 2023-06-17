import {Component, OnInit, ViewChild} from '@angular/core';
import {DeviceDetailsResponse} from "../../../../model/Device";
import {ActivatedRoute} from "@angular/router";
import {DeviceManagementService} from "../../../../services/device.service";
import {LogSource, LogTableDataSource} from "../../../../model/LogTableDataSource";
import {LogService} from "../../../../services/log.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-device-container',
  templateUrl: './device-container.component.html',
  styleUrls: ['./device-container.component.css']
})
export class DeviceContainerComponent implements OnInit {
  device = new DeviceDetailsResponse();
  deviceId: string | null = "";
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  displayedColumns = ["message", "type", "time"];
  logType = "all";
  logSources = [
    {value: LogSource.DEVICE_MONITORING, description: LogSource.DEVICE_MONITORING.valueOf()},
    {value: LogSource.DEVICE_MANAGEMENT, description: LogSource.DEVICE_MANAGEMENT.valueOf()},
    {value: LogSource.AUTHENTICATION, description: LogSource.AUTHENTICATION.valueOf()},
    {value: LogSource.AUTHORIZATION, description: LogSource.AUTHORIZATION.valueOf()},
    {value: LogSource.CERTIFICATE_MANAGEMENT, description: LogSource.CERTIFICATE_MANAGEMENT.valueOf()},
  ];
  dataSource: LogTableDataSource;
  source: LogSource = LogSource.DEVICE_MONITORING;
  search = "";

  campaignOne = new FormGroup({
    start: new FormControl(new Date),
    end: new FormControl(new Date),
  });

  constructor(private router: ActivatedRoute, private deviceService: DeviceManagementService, private logsService: LogService) {
    this.dataSource = new LogTableDataSource(logsService);
  }

  ngOnInit(): void {
    this.deviceId = this.router.snapshot.paramMap.get('id');
    this.deviceService.getDevice(this.deviceId).subscribe({
      next: value => this.device = value
    })
  }

  applyFilter() {
    this.dataSource.loadLogs(
      this.source,
      this.search,
      this.logType
    );
  }

  downloadReport() {
    this.deviceService.getReport(this.campaignOne.value.start, this.campaignOne.value.end).subscribe({
      next: (pdf: Blob) => {
        const element = document.createElement('a');
        // const file = new Blob([content], {type: 'text/plain'});
        element.href = URL.createObjectURL(pdf);
        element.download = "report.pdf";
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
      }
    })
  }
}
