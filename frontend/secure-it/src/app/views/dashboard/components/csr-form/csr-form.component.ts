import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CsrService} from "../../../../services/csr.service";
import {MatDialogRef} from "@angular/material/dialog";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-csr-form',
  templateUrl: './csr-form.component.html',
  styleUrls: ['./csr-form.component.css']
})
export class CsrFormComponent {
  csrForm: FormGroup;
  commonName: string = "";
  @Input() dialogRef!: MatDialogRef<CsrFormComponent>;

  constructor(private fb: FormBuilder, private csrService: CsrService, private authService: AuthService) {
    this.csrForm = this.fb.group({
      organization: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', [Validators.required, CountryCodeValidator]],
      algorithm: ['RSA', [Validators.required, Validators.pattern(/^(RSA|DSA|EC)$/)]],
      keySize: ['2048', [Validators.required, Validators.min(1)]]
    });
    this.authService.getCurrentlyLoggedUser().subscribe((user) => {
      this.commonName = user.email
    })
  }

  onSubmit() {
    if (this.csrForm.valid) {
      const data = this.csrForm.value;
      this.csrService.createCsr(data).subscribe({
        next: (csr) => console.log(csr),
        error: () => console.error("Error")
      })
    }
  }

  get organization() {
    return this.csrForm.get('organization');
  }

  get city() {
    return this.csrForm.get('city');
  }

  get state() {
    return this.csrForm.get('state');
  }

  get country() {
    return this.csrForm.get('country');
  }

  get algorithm() {
    return this.csrForm.get('algorithm');
  }

  get keySize() {
    return this.csrForm.get('keySize');
  }
}

function CountryCodeValidator(control: { value: any; }) {
  const countryCode = control.value;
  if (countryCode && countryCode.length === 2) {
    return null;
  } else {
    return {invalidCountryCode: true};
  }
}
