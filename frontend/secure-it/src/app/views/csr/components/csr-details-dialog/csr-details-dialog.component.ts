import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CertificateDetails} from "../../../../model/CertificateDetails";
import {CertificateService} from "../../../../services/certificate.service";

@Component({
  selector: 'app-csr-details-dialog',
  templateUrl: './csr-details-dialog.component.html',
  styleUrls: ['./csr-details-dialog.component.css']
})
export class CsrDetailsDialogComponent {
  @Input() serialNumber!: BigInteger;
  @Input() dialogRef!: MatDialogRef<CsrDetailsDialogComponent>;

  certificate: CertificateDetails = new CertificateDetails();

  constructor(certificateService: CertificateService) {
    certificateService.getBySerialNumber(this.serialNumber).subscribe(
      (cer) => this.certificate = cer
    );
  }

}
