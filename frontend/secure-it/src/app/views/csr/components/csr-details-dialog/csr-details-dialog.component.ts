import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrDetails} from "../../../../model/CsrDetails";
import {CsrService} from "../../../../services/csr.service";
import {getDateTime} from "../../../../utils/TimeUtils";

@Component({
  selector: 'app-csr-details-dialog',
  templateUrl: './csr-details-dialog.component.html',
  styleUrls: ['./csr-details-dialog.component.css']
})
export class CsrDetailsDialogComponent {
  @Input() id!: string;
  @Input() dialogRef!: MatDialogRef<CsrDetailsDialogComponent>;

  csr: CsrDetails = new CsrDetails();

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

  parseDate(created: Date) {
    return getDateTime(created.toString());
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

  reject(id: string) {

  }

  approve(id: string) {

  }
}
