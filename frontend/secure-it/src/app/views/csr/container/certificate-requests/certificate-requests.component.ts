import {Component, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CsrFormComponent} from "../../../dashboard/components/csr-form/csr-form.component";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CsrService} from "../../../../services/csr.service";

@Component({
  selector: 'app-certificate-requests',
  templateUrl: './certificate-requests.component.html',
  styleUrls: ['./certificate-requests.component.css']
})
export class CertificateRequestsComponent {

  @ViewChild(MatPaginator, {static: false}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort!: MatSort;
  requestStatus = "accepted";

  constructor(private dialog: MatDialog, private csrService: CsrService) {
    this.dataSource = new C
  }

  newCertificate() {
    let dialogRef = this.dialog.open(CsrFormComponent, {
      height: '400px',
      width: '600px',
    });
  }
}
