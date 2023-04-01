import {Component, Input} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CsrFormComponent} from "../../../dashboard/components/csr-form/csr-form.component";

@Component({
  selector: 'app-certificate-requests',
  templateUrl: './certificate-requests.component.html',
  styleUrls: ['./certificate-requests.component.css']
})
export class CertificateRequestsComponent {

  @Input() id!: string;

  constructor(private dialog: MatDialog) {
  }

  newCertificate() {
    let dialogRef = this.dialog.open(CsrFormComponent, {
      height: '400px',
      width: '600px',
    });
  }
}
