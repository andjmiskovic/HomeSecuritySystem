import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrDetails} from "../../../../model/CsrDetails";
import {CsrService} from "../../../../services/csr.service";
import {getDateTime} from "../../../../utils/TimeUtils";
import {CertificateCreationOptions} from "../../../../model/CertificateCreationOptions";

@Component({
  selector: 'app-csr-details-dialog',
  templateUrl: './csr-details-dialog.component.html',
  styleUrls: ['./csr-details-dialog.component.css']
})
export class CsrDetailsDialogComponent {
  @Input() id!: string;
  @Input() dialogRef!: MatDialogRef<CsrDetailsDialogComponent>;

  csr = new CsrDetails();
  reason = "";
  options = new CertificateCreationOptions();
  inProcess = "none";

  constructor(private csrService: CsrService) {
  }

  ngOnInit() {
    this.csrService.getCsrById(this.id).subscribe(
      (csr) => {
        this.csr = csr;
        console.log(csr)
      }
    );
  }

  parseDate(date: Date) {
    try {
      return getDateTime(date.toString());
    } catch (e) {
      return 'N/A';
    }
  }

  downloadCsrPem() {
    this.downloadTxtFile(this.csr.csrPem, "csrPem.txt");
  }

  downloadPublicKeyPem() {
    this.downloadTxtFile(this.csr.publicKeyPem, "publicKeyPem.txt");
  }

  downloadTxtFile(content: string, fileName: string): void {
    const element = document.createElement('a');
    const file = new Blob([content], {type: 'text/plain'});
    element.href = URL.createObjectURL(file);
    element.download = fileName;
    document.body.appendChild(element);
    element.click();
    document.body.removeChild(element);
  }

  reject() {
    this.csrService.rejectRequest(this.id, this.reason);
  }

  issue() {
    this.csrService.issueCertificate(this.id, this.options);
  }
}
