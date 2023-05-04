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
    this.options.extensions = {
      "subjectKeyIdentifier": this.subjectKeyIdentifier,
      "authorityKeyIdentifier": this.authorityKeyIdentifier,
      "keyUsage": this.chosenOptionsFormControl?.value
    };

    if (this.template === '1')
      this.options.extensions["subjectAlternativeName"] = this.subjectAlternativeName;
  }

  approve() {
    this.setOptions();
    this.csrService.issueCertificate(this.id, this.options).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }

  trackByFn(index: any, item: any) {
    return index;
  }

  validForm(): boolean {
    if (this.chosenOptionsFormControl.value?.length == 0)
      return false;
    if (this.template == '1' && this.subjectAlternativeName.length == 0)
      return false;
    if (this.template == '1') {
      for (let x of this.subjectAlternativeName) {
        if (x.length == 0)
          return false;
      }
    }
    return true;
  }
}
