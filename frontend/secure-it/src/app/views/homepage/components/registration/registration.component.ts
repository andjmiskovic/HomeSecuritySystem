import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {AbstractControl, FormBuilder, ValidatorFn, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {RegisterCredentials} from "../../../../model/RegisterCredentials";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  @Output() switchForm = new EventEmitter();

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    phoneFormControl: ['phoneNumber', [Validators.required]],
    nameFormControl: ['name', [Validators.required]],
    lastNameFormControl: ['lastName', [Validators.required]],
    cityFormControl: ['city', [Validators.required]],
    passwordFormControl: ['', [Validators.required, Validators.minLength(12), this.passwordValidator()]],
    password2FormControl: ['password2', [Validators.required]]
  });

  email = "";
  phoneNumber = "";
  password = "";
  password2 = "";
  name = "";
  lastName = "";
  city = "";

  hide = true;
  hide2 = true;

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
      console.log(requestBody)
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

  passwordValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{12,}$/;
      const valid = passwordRegex.test(control.value);
      return valid ? null : {invalidPassword: true};
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
