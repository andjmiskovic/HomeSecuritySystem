import {Component, Input} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CsrDetails} from "../../../../model/CsrDetails";
import {CsrService} from "../../../../services/csr.service";
import {getDateTime} from "../../../../utils/TimeUtils";
import {CertificateCreationOptions} from "../../../../model/CertificateCreationOptions";
import {RejectCsrComponent} from "../reject-csr/reject-csr.component";
import {ApproveCsrComponent} from "../approve-csr/approve-csr.component";
import {downloadTxtFile} from "../../../../utils/DownloadFile";

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
  userRole: string;

  constructor(private csrService: CsrService, public dialog: MatDialog) {
    this.userRole = localStorage.getItem("userRole") || ""
  }

  ngOnInit() {
    this.loadCsr();
  }

  loadCsr() {
    this.csrService.getCsrById(this.id).subscribe((csr) => this.csr = csr);
  }

  parseDate(date: Date) {
    try {
      return getDateTime(date.toString());
    } catch (e) {
      return 'N/A';
    }
  }

  downloadCsrPem() {
    downloadTxtFile(this.csr.csrPem, "csrPem.txt");
  }

  downloadPublicKeyPem() {
    downloadTxtFile(this.csr.publicKeyPem, "publicKeyPem.txt");
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
