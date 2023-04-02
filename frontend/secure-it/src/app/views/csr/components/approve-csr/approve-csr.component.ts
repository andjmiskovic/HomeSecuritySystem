import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrService} from "../../../../services/csr.service";
import {CertificateCreationOptions} from "../../../../model/CertificateCreationOptions";

@Component({
  selector: 'app-approve-csr',
  templateUrl: './approve-csr.component.html',
  styleUrls: ['./approve-csr.component.css']
})
export class ApproveCsrComponent {
  @Input() id!: string;

  issuer = "1680141471";
  options = new CertificateCreationOptions();

  constructor(public dialogRef: MatDialogRef<ApproveCsrComponent>, private csrService: CsrService) {
  }

  onNoClick() {
    this.dialogRef.close();
  }

  approve() {
    this.csrService.issueCertificate(this.id, this.options).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }
}
