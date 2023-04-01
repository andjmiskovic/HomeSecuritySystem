import {Component} from '@angular/core';
import {MatOption} from "@angular/material/core";

@Component({
  selector: 'app-csr-list',
  templateUrl: './csr-list.component.html',
  styleUrls: ['./csr-list.component.css']
})
export class CsrListComponent {
  // displayedColumns = ["issuedTo", "issuedBy", "validFrom", "validTo", "details"];
  // dataSource: CertificateTableDataSource;
  //
  // @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  // @ViewChild(MatSort, {static: false}) sort!: MatSort;
  requestStatus: string = "accepted";
  //
  // constructor(public dialog: MatDialog) {
  //   this.dataSource = new CertificateTableDataSource();
  // }
  //
  // ngAfterViewInit() {
  //   this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  //   merge(this.sort.sortChange, this.paginator.page)
  //     .pipe(
  //       tap(() => this.loadCertificates())
  //     )
  //     .subscribe();
  // }
  //
  // loadCertificates() {
  //   this.dataSource.loadCertificates(
  //     this.sort.active,
  //     this.sort.direction,
  //     this.paginator.pageIndex,
  //     this.paginator.pageSize);
  // }
  //
  // detailsAboutCertificate(id: number) {
  //   const dialogRef = this.dialog.open(CsrDetailsDialogComponent, {
  //     height: '600px',
  //     width: '1000px'
  //   });
  //   dialogRef.componentInstance.id = id;
  //   dialogRef.componentInstance.dialogRef = dialogRef;
  // }
  //
  applyFilter($event: KeyboardEvent) {

  }
}
