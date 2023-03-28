import {Component, ViewChild} from '@angular/core';
import {CertificateTableDataSource} from "../../model/CertificateTableData";
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {merge, tap} from "rxjs";

@Component({
  selector: 'app-certificate-list',
  templateUrl: './certificate-list.component.html',
  styleUrls: ['./certificate-list.component.css']
})
export class CertificateListComponent {
  displayedColumns = ["issuedTo", "issuedBy", "validFrom", "validTo", "details"];
  dataSource: CertificateTableDataSource;

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;

  constructor(public dialog: MatDialog) {
    this.dataSource = new CertificateTableDataSource();
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadCertificates())
      )
      .subscribe();
  }

  loadCertificates() {
    this.dataSource.loadCertificates(
      this.sort.active,
      this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  detailsAboutCertificate(id: number) {
    // const dialogRef = this.dialog.open(RideDetailsDialogComponent, {
    //   height: '600px',
    //   width: '1000px'
    // });
    // dialogRef.componentInstance.id = id;
    // dialogRef.componentInstance.dialogRef = dialogRef;
  }
}
