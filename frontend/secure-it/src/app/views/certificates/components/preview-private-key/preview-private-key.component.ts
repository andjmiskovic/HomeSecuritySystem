import {Component, Inject, Input} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {Router} from "@angular/router";
import {LoginCredentials} from "../../../../model/LoginCredentials";
import {CertificateService} from "../../../../services/certificate.service";
import {downloadTxtFile} from "../../../../utils/DownloadFile";

@Component({
  selector: 'app-preview-private-key',
  templateUrl: './preview-private-key.component.html',
  styleUrls: ['./preview-private-key.component.css']
})
export class PreviewPrivateKeyComponent {
  @Input() serialNumber!: BigInteger;

  formGroup = this._formBuilder.group({
    passwordFormControl: ['password', [Validators.required]]
  })

  hide = true;
  password = "";

  constructor(@Inject(MatSnackBar) private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private certificateService: CertificateService, private router: Router) {
  }

  previewPrivateKey() {
    this.certificateService.getPrivateKey(this.serialNumber, this.password).subscribe({
      next: (res) => {
        downloadTxtFile(res, "privateKeyPem.txt")
      },
      error: (res) => {
        if (res.error.text)
          downloadTxtFile(res.error.text, "privateKeyPem.txt")
      }
    })
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }
}
