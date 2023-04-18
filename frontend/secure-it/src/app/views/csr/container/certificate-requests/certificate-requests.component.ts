import {Component, ViewChild, Input} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CsrService} from "../../../../services/csr.service";
import {CsrTableDataSource} from "../../../../model/CsrTableData";
import {MatSelectChange} from "@angular/material/select";
import {CsrDetailsDialogComponent} from "../../components/csr-details-dialog/csr-details-dialog.component";

@Component({
  selector: 'app-certificate-requests',
  templateUrl: './certificate-requests.component.html',
  styleUrls: ['./certificate-requests.component.css']
})
export class CertificateRequestsComponent {
  displayedColumns = ["alias", "commonName", "organization", "status", "details"];
  dataSource: CsrTableDataSource;

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;
  requestStatus = "accepted";
  @Input() id!: string;
  selectedFilter: string;
  searchFilter: string;

  constructor(private dialog: MatDialog, private csrService: CsrService) {
    this.dataSource = new CsrTableDataSource(csrService);
    this.selectedFilter = "all"
    this.searchFilter = ""
  }

  ngAfterViewInit() {
    this.loadCsrs();
  }

  applySearchFilter($event: KeyboardEvent) {
    this.loadCsrs()
  }

  private loadCsrs() {
    this.dataSource.loadCsrs(
      this.selectedFilter,
      this.searchFilter
    );
  }

  detailsAboutCsr(id: string) {
    let dialogRef = this.dialog.open(CsrDetailsDialogComponent, {
      height: '650px',
      width: '800px'
    });
    dialogRef.componentInstance.id = id;
    dialogRef.afterClosed().subscribe(() => this.loadCsrs());
  }

  selectedFilterChange($event: MatSelectChange) {
    this.loadCsrs();
  }
}
