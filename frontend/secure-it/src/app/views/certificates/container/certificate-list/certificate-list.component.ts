import {Component, ViewChild} from '@angular/core';
import {CertificateTableDataSource} from "../../../../model/CertificateTableData";
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {merge, tap} from "rxjs";
import {
  CertificatesDetailsDialogComponent
} from "../../components/certificates-details-dialog/certificates-details-dialog.component";
import {CertificateService} from "../../../../services/certificates.service";
import {getDateTime} from "../../../../utils/TimeUtils";

@Component({
  selector: 'app-certificate-list',
  templateUrl: './certificate-list.component.html',
  styleUrls: ['./certificate-list.component.css']
})
export class CertificateListComponent {
  displayedColumns = ["serialNumber", "alias", "notBefore", "notAfter", "details"];
  dataSource: CertificateTableDataSource;
  searchFilter: string;

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  constructor(public dialog: MatDialog, private certificateService: CertificateService) {
    this.dataSource = new CertificateTableDataSource(certificateService);
    this.searchFilter = ""
  }

  ngAfterViewInit() {
    this.loadCertificates()
    // this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    // merge(this.sort.sortChange, this.paginator.page)
    //   .pipe(
    //     tap(() => this.loadCertificates())
    //   )
    //   .subscribe();
  }

  loadCertificates() {
    this.dataSource.loadCertificates(
      this.searchFilter
      // this.sort.active,
      // this.sort.direction
    );
  }

  detailsAboutCertificate(id: number) {
    const dialogRef = this.dialog.open(CertificatesDetailsDialogComponent, {
      height: '600px',
      width: '1000px'
    });
    dialogRef.componentInstance.id = id;
    dialogRef.componentInstance.dialogRef = dialogRef;
  }

  applyFilter($event: KeyboardEvent) {
    this.loadCertificates()
  }


  parseDate(date: string) {
    return getDateTime(date)
  }
}
