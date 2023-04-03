import {Component, ViewChild} from '@angular/core';
import {CertificateTableDataSource} from "../../../../model/CertificateTableData";
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {
  CertificatesDetailsDialogComponent
} from "../../components/certificates-details-dialog/certificates-details-dialog.component";
import {getDateTime} from "../../../../utils/TimeUtils";
import {CertificateDetails} from "../../../../model/CertificateDetails";
import {CertificateService} from "../../../../services/certificate.service";

@Component({
  selector: 'app-certificate-list',
  templateUrl: './certificate-list.component.html',
  styleUrls: ['./certificate-list.component.css']
})
export class CertificateListComponent {
  displayedColumns = ["serialNumber", "alias", "notBefore", "notAfter", "validityStatus", "details"];
  dataSource: CertificateTableDataSource;
  searchFilter: string;

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  constructor(public dialog: MatDialog, private certificateService: CertificateService) {
    this.dataSource = new CertificateTableDataSource(certificateService);
    this.searchFilter = ""
  }

  ngAfterViewInit() {
    this.loadCertificates();
  }

  loadCertificates() {
    this.dataSource.loadCertificates(
      this.searchFilter
    );
  }

  detailsAboutCertificate(certificate: CertificateDetails) {
    const dialogRef = this.dialog.open(CertificatesDetailsDialogComponent, {
      height: '600px',
      width: '1000px'
    });
    dialogRef.componentInstance.serialNumber = certificate.serialNumber;
    dialogRef.componentInstance.dialogRef = dialogRef;
    dialogRef.afterClosed().subscribe(() => this.loadCertificates());
  }

  applyFilter($event: KeyboardEvent) {
    this.loadCertificates();
  }

  parseDate(date: string) {
    return getDateTime(date);
  }
}
