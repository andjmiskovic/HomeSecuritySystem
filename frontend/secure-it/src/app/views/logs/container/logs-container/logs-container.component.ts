import {Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {LogTableDataSource} from "../../../../model/LogTableDataSource";
import {LogService} from "../../../../services/log.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-logs-container',
  templateUrl: './logs-container.component.html',
  styleUrls: ['./logs-container.component.css']
})
export class LogsContainerComponent {
  displayedColumns = ["message", "type", "time"];
  dataSource: LogTableDataSource;
  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  campaignOne = new FormGroup({
    start: new FormControl(new Date),
    end: new FormControl(new Date),
  });
  regex = "";

  constructor(private logService: LogService) {
    this.dataSource = new LogTableDataSource(logService);
  }

  applySearchFilter($event: KeyboardEvent) {
    
  }
}
