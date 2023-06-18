import {Component, Input, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {LogSource, LogTableDataSource, LogType} from "../../../../model/LogTableDataSource";
import {LogService} from "../../../../services/log.service";

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent {
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  @Input() deviceId: string = '';

  displayedColumns = ["message", "type", "time"];
  dataSource: LogTableDataSource;
  logType: LogType | null = null;
  logSources = [
    {value: LogSource.DEVICE_MONITORING, description: LogSource.DEVICE_MONITORING.valueOf()},
    {value: LogSource.DEVICE_MANAGEMENT, description: LogSource.DEVICE_MANAGEMENT.valueOf()},
    {value: LogSource.AUTHENTICATION, description: LogSource.AUTHENTICATION.valueOf()},
    {value: LogSource.AUTHORIZATION, description: LogSource.AUTHORIZATION.valueOf()},
    {value: LogSource.CERTIFICATE_MANAGEMENT, description: LogSource.CERTIFICATE_MANAGEMENT.valueOf()},
  ];
  source: LogSource = LogSource.DEVICE_MONITORING;
  regex = "";

  constructor(private logsService: LogService) {
    this.dataSource = new LogTableDataSource(logsService);
  }

  applyFilter() {
    this.dataSource.loadLogs(
      this.regex,
      this.source,
      this.deviceId,
      this.logType
    );
  }
}
