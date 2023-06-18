import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {LogSource, LogTableDataSource, LogType} from "../../../../model/LogTableDataSource";
import {LogService} from "../../../../services/log.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-logs-container',
  templateUrl: './logs-container.component.html',
  styleUrls: ['./logs-container.component.css']
})
export class LogsContainerComponent implements OnInit {
  displayedColumns = ["message", "type", "timestamp"];
  dataSource: LogTableDataSource;
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  campaignOne = new FormGroup({
    start: new FormControl(new Date),
    end: new FormControl(new Date),
  });
  regex = "";
  sourceId = "";
  type: LogType | null = null;
  source: LogSource | null = null;

  logSources = [
    {value: LogSource.DEVICE_MONITORING, description: LogSource.DEVICE_MONITORING.valueOf()},
    {value: LogSource.DEVICE_MANAGEMENT, description: LogSource.DEVICE_MANAGEMENT.valueOf()},
    {value: LogSource.AUTHENTICATION, description: LogSource.AUTHENTICATION.valueOf()},
    {value: LogSource.AUTHORIZATION, description: LogSource.AUTHORIZATION.valueOf()},
    {value: LogSource.CERTIFICATE_MANAGEMENT, description: LogSource.CERTIFICATE_MANAGEMENT.valueOf()},
  ];

  constructor(private logService: LogService) {
    this.dataSource = new LogTableDataSource(logService);
  }

  ngOnInit() {
    this.loadLogs();
  }

  loadLogs() {
    this.dataSource.loadLogs(this.regex, this.source, this.sourceId, this.type);
  }
}
