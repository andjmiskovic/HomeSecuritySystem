import {Component, Input} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CsrDetails} from "../../../../model/CsrDetails";
import {CsrService} from "../../../../services/csr.service";
import {getDateTime} from "../../../../utils/TimeUtils";
import {CertificateCreationOptions} from "../../../../model/CertificateCreationOptions";
import {RejectCsrComponent} from "../reject-csr/reject-csr.component";
import {ApproveCsrComponent} from "../approve-csr/approve-csr.component";

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

  constructor(private csrService: CsrService, public dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadCsr();
  }

  loadCsr() {
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
    const dialogRef = this.dialog.open(RejectCsrComponent);
    dialogRef.componentInstance.id = this.id;
    dialogRef.afterClosed().subscribe(() => this.loadCsr());
  }

  issue() {
    const dialogRef = this.dialog.open(ApproveCsrComponent);
    dialogRef.componentInstance.id = this.id;
    dialogRef.afterClosed().subscribe(() => this.loadCsr());
  }
}
