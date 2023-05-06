import {Component, ViewChild} from '@angular/core';
import {CertificateTableDataSource} from "../../../../model/CertificateTableData";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatDialog} from "@angular/material/dialog";
import {CertificateService} from "../../../../services/certificate.service";
import {BlacklistedTokensTableDataSource} from "../../../../model/BlacklistedTokensTableDataSource";
import {TokenService} from "../../../../services/token.service";
import {getDateTime} from "../../../../utils/TimeUtils";

@Component({
  selector: 'app-blacklist-table',
  templateUrl: './blacklist-table.component.html',
  styleUrls: ['./blacklist-table.component.css']
})
export class BlacklistTableComponent {
  displayedColumns = ["subject", "issuedAt", "expiresAt", "error", "added"];
  dataSource: BlacklistedTokensTableDataSource;
  userRole: string;

  constructor(public dialog: MatDialog, private tokenService: TokenService) {
    this.userRole = localStorage.getItem("userRole") || ""
    this.dataSource = new BlacklistedTokensTableDataSource(tokenService, this.userRole);
  }

  ngAfterViewInit() {
    this.loadBlacklist();
  }

  loadBlacklist() {
    this.dataSource.loadBlacklist();
  }
  parseDate(date: string) {
    return getDateTime(date);
  }
}
