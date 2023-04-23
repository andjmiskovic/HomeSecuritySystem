import {Component, Inject} from '@angular/core';
import {AbstractControl, FormBuilder, ValidatorFn, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../../services/auth.service";
import {RegisterCredentials} from "../../../../model/RegisterCredentials";
import {SetPasswordRequest} from "../../../../model/SetPasswordRequest";

@Component({
  selector: 'app-enter-password',
  templateUrl: './enter-password.component.html',
  styleUrls: ['./enter-password.component.css']
})
export class EnterPasswordComponent {
  formGroup = this._formBuilder.group({
    passwordFormControl: ['', [Validators.required, Validators.minLength(12), this.passwordValidator()]],
    password2FormControl: ['password2', [Validators.required]]
  });

  password!: string;
  password2! : string;
  hide = true;
  hide2 = true;
  verificationCode!: string;

  constructor(
    @Inject(MatSnackBar) private _snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private _formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.verificationCode = params['code'] || "";
      // this.authService.verify(params['code'] || "").subscribe((res) => {
      //   console.log(res)
      // })
    });
  }

  navigate() {
    this.router.navigate(['/']);
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

  setPassword() {
    if (this.password !== this.password2) {
      this.openSnackBar("Passwords are not the same.");
    } else {
      const requestBody: SetPasswordRequest = {
        "password": this.password,
        "passwordConfirmation": this.password2,
        "verificationCode": this.verificationCode
      }
      this.authService.setPassword(requestBody).subscribe({
        next: () => {
          this.openSnackBar("You have successfully set your password")
          this.router.navigate(['/']);
        },
        error: (message) => this.openSnackBar(message)
      });
    }
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }
}
