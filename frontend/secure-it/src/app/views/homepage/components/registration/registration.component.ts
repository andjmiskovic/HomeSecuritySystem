import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {AbstractControl, FormBuilder, ValidatorFn, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {RegisterCredentials} from "../../../../model/RegisterCredentials";
import {passwordValidator, phoneNumberValidator} from "../../../../utils/InputValidation";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  @Output() switchForm = new EventEmitter();

  email = "";
  phoneNumber = "";
  password = "";
  password2 = "";
  name = "";
  lastName = "";
  city = "";

  hide = true;
  hide2 = true;

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    phoneFormControl: ['phoneNumber', [Validators.required, phoneNumberValidator()]],
    nameFormControl: ['name', [Validators.required]],
    lastNameFormControl: ['lastName', [Validators.required]],
    cityFormControl: ['city', [Validators.required]],
    passwordFormControl: ['', [Validators.required, Validators.minLength(12), passwordValidator()]],
    password2FormControl: ['password2', [Validators.required, this.passwordMatchValidator()]]
  });

  constructor(@Inject(MatSnackBar) private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService) {
  }

  registerNewUser() {
    if (this.password !== this.password2) {
      this.openSnackBar("Passwords are not the same.");
    } else {
      const requestBody: RegisterCredentials = {
        "email": this.email,
        "password": this.password,
        "firstName": this.name,
        "lastName": this.lastName,
        "phoneNumber": this.phoneNumber,
        "city": this.city,
        "passwordConfirmation": this.password2
      }
      this.authService.register(requestBody).subscribe({
        next: () => this.openSnackBar("We sent you registration link"),
        error: (message) => this.openSnackBar(message)
      });
    }
  }

  largePasswordErrorMessage() {
    return this.formGroup.hasError('invalidPassword', 'passwordFormControl')
      && !this.formGroup.hasError('required', 'passwordFormControl')
      && !this.formGroup.hasError('minlength', 'passwordFormControl');
  }

  invalidPhoneNumberErrorMessage() {
    return this.formGroup.hasError('invalidPhoneNumber', 'phoneFormControl')
      && !this.formGroup.hasError('required', 'phoneFormControl');
  }

  notMatchingPasswords() {
    return this.formGroup.hasError('invalidRepeatPassword', 'password2FormControl')
      && !this.formGroup.hasError('required', 'password2FormControl');
  }

  passwordMatchValidator() {
    return (control: AbstractControl): { [key: string]: any } | null => {
      return this.password === control.value ? null : {invalidRepeatPassword: true};
    };
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

  switchToLoginForm() {
    this.switchForm.emit();
  }


}
