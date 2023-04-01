import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CsrService} from "../../../../services/csr.service";

@Component({
  selector: 'app-csr-form',
  templateUrl: './csr-form.component.html',
  styleUrls: ['./csr-form.component.css']
})
export class CsrFormComponent {
  csrForm: FormGroup;

  constructor(private fb: FormBuilder, private csrService: CsrService) {
    this.csrForm = this.fb.group({
      organization: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', [Validators.required, CountryCodeValidator]],
      algorithm: ['', [Validators.required, Validators.pattern(/^(RSA|DSA|EC)$/)]],
      keySize: ['', [Validators.required, Validators.min(1)]]
    });
  }

  onSubmit() {
    if (this.csrForm.valid) {
      const data = this.csrForm.value;
      this.csrService.createCsr(data).subscribe({
        next: () => console.log(""),
        error: () => console.error("")
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
