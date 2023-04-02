import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrService} from "../../../../services/csr.service";
import {CertificateCreationOptions} from "../../../../model/CertificateCreationOptions";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-approve-csr',
  templateUrl: './approve-csr.component.html',
  styleUrls: ['./approve-csr.component.css']
})
export class ApproveCsrComponent {
  @Input() id!: string;

  options = new CertificateCreationOptions();
  template = "1";
  chosenOptionsFormControl = new FormControl('');
  keyUsageOptions = [
    ["encipherOnly", "Encipher Only"],
    ["cRLSign", "Certificate Revocation List Signing"],
    ["keyCertSign", "Key Certificate Signing"],
    ["keyAgreement", "Key Agreement"],
    ["dataEncipherment", "Data Encipherment"],
    ["keyEncipherment", "Key Encipherment"],
    ["nonRepudiation", "Non-Repudiation"],
    ["digitalSignature", "Digital Signature"],
    ["decipherOnly", "Decipher Only"]
  ];

  subjectAlternativeName: string[] = [""];
  subjectKeyIdentifier = false;
  authorityKeyIdentifier = false;

  constructor(public dialogRef: MatDialogRef<ApproveCsrComponent>, private csrService: CsrService) {
  }

  onNoClick() {
    this.dialogRef.close();
  }

  addNewInstance() {
    this.subjectAlternativeName.push("");
  }

  removeInstance(i: number) {
    this.subjectAlternativeName.splice(i, 1);
  }

  approve() {
    this.csrService.issueCertificate(this.id, this.options).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }
}
