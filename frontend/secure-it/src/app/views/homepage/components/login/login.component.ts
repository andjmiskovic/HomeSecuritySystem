import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {Router} from '@angular/router';
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoginCredentials} from "../../../../model/LoginCredentials";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Output() switchForm = new EventEmitter();

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    passwordFormControl: ['password', [Validators.required]]
  })

  hide = true;

  email = "";
  password = "";

  constructor(@Inject(MatSnackBar) private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
  }

  postLogin(accessToken: string) {
    localStorage.setItem('token', "Bearer " + accessToken);
    this.authService.getCurrentlyLoggedUser().subscribe({
      next: (user) => {
        if (user.role === "CUSTOMER")
          this.router.navigate(['/dashboard']);
        else if (user.role === "ADMIN")
          this.router.navigate(['/dashboard']);
      }
    });
  }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    let loginCredentials: LoginCredentials = {
      email: this.email,
      password: this.password
    }
    this.authService.login(loginCredentials).subscribe({
      next: (loginResponse) => {
        localStorage.setItem('token', "Bearer " + loginResponse.accessToken);
        this.router.navigate(['/certificates'])
      }
    })
  }

  openSnack(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

}
