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
  chosenOptionsFormControl!: FormControl;
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
    this.chosenOptionsFormControl = new FormControl('')
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

  setOptions() {
    this.options.extensions["subjectKeyIdentifier"] = this.subjectAlternativeName.toString();
    this.options.extensions["authorityKeyIdentifier"] = this.authorityKeyIdentifier.toString();

    let list = [];
    for (let option of this.chosenOptionsFormControl?.value) {
      list.push(option[0]);
    }
    this.options.extensions["keyUsage"] = list;
    if (this.template === '1')
      this.options.extensions["subjectAlternativeName"] = this.subjectAlternativeName;
  }

  approve() {
    this.setOptions();
    console.log(this.options);
    this.csrService.issueCertificate(this.id, this.options).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }
}
