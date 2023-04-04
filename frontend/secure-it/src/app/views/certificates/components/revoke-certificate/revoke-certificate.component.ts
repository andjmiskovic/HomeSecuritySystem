import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CertificateService} from "../../../../services/certificate.service";
import {CertificateRevocationRequest} from "../../../../model/CertificateRevocationRequest";

@Component({
  selector: 'app-revoke-certificate',
  templateUrl: './revoke-certificate.component.html',
  styleUrls: ['./revoke-certificate.component.css']
})
export class RevokeCertificateComponent {
  @Input() serialNumber!: BigInteger;

  reason: CertificateRevocationRequest = new CertificateRevocationRequest();
  commonReasons = [
    'Misuse or Abuse of Certificate',
    'Change in Certificate Policy',
    'Compromise of Private Key',
    'Administrative Error',
  ];
  otherReason: boolean = false;

  constructor(public dialogRef: MatDialogRef<RevokeCertificateComponent>, private certificateService: CertificateService) {
  }

  onNoClick() {
    this.dialogRef.close();
  }

  revoke() {
    this.certificateService.revokeCertificate(this.serialNumber, this.reason).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }
}
