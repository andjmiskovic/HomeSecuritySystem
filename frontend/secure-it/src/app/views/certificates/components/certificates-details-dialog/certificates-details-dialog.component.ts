import {Component, Input, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CertificateDetails} from "../../../../model/CertificateDetails";
import {CertificateService} from "../../../../services/certificate.service";
import {CertificateValidityResponse} from "../../../../model/CertificateValidityResponse";
import {RevokeCertificateComponent} from "../revoke-certificate/revoke-certificate.component";
import {getDateTime} from "../../../../utils/TimeUtils";

@Component({
  selector: 'app-certificates-details-dialog',
  templateUrl: './certificates-details-dialog.component.html',
  styleUrls: ['./certificates-details-dialog.component.css']
})
export class CertificatesDetailsDialogComponent implements OnInit {
  @Input() serialNumber!: BigInteger;
  @Input() dialogRef!: MatDialogRef<CertificatesDetailsDialogComponent, any>;

  certificate: CertificateDetails = new CertificateDetails();
  validity: CertificateValidityResponse = new CertificateValidityResponse();

  constructor(private certificateService: CertificateService, public dialog: MatDialog) {
  }

  ngOnInit() {
    this.certificateService.getBySerialNumber(this.serialNumber).subscribe(
      (cer) => this.certificate = cer
    );
    this.checkValidity();
  }

  checkValidity() {
    this.certificateService.checkCertificateValidity(this.serialNumber).subscribe(
      (validity) => this.validity = validity
    );
  }

  revokeCertificate(serialNumber: BigInteger) {
    const dialogRef = this.dialog.open(RevokeCertificateComponent);
    dialogRef.componentInstance.serialNumber = serialNumber;
    dialogRef.afterClosed().subscribe(() => this.checkValidity());
  }

  parseDate(date: Date) {
    try {
      return getDateTime(date.toString());
    } catch (e) {
      return 'N/A';
    }
  }
}
