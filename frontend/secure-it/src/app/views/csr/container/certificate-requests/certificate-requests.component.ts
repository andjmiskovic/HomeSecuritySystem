import {CertificateTableDataSource} from "../../../../model/CertificateTableData";

import {Component, ViewChild, Input} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CsrFormComponent} from "../../../dashboard/components/csr-form/csr-form.component";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CsrService} from "../../../../services/csr.service";
import {CsrTableDataSource} from "../../../../model/CsrTableData";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-certificate-requests',
  templateUrl: './certificate-requests.component.html',
  styleUrls: ['./certificate-requests.component.css']
})
export class CertificateRequestsComponent {
  displayedColumns = ["alias", "organization", "algorithm", "keySize", "status", "details"];
  dataSource: CsrTableDataSource;

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;
  requestStatus = "accepted";
  @Input() id!: string;
  selectedFilter: string;

  constructor(private dialog: MatDialog, private csrService: CsrService) {
    this.dataSource = new CsrTableDataSource(csrService);
    this.selectedFilter = "all"
  }

  ngAfterViewInit() {
    this.loadCsrs()
    // this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    // merge(this.sort.sortChange, this.paginator.page)
    //   .pipe(
    //     tap(() => this.loadCertificates())
    //   )
    //   .subscribe();
  }

  newCertificate() {
    let dialogRef = this.dialog.open(CsrFormComponent, {
      height: '400px',
      width: '600px',
    });
  }

  applyFilter($event: KeyboardEvent) {

  }

  private loadCsrs() {
    this.dataSource.loadCsrs(
      this.selectedFilter
    );
  }

  detailsAboutCsr(id: BigInteger) {

  }

  selectedFilterChange($event: MatSelectChange) {
    this.loadCsrs()
  }
}
