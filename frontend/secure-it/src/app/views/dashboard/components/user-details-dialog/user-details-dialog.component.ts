import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {CreateUserCredentials} from "../../../../model/RegisterCredentials";
import {UserService} from "../../../../services/user.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-user-details-dialog',
  templateUrl: './user-details-dialog.component.html',
  styleUrls: ['./user-details-dialog.component.css']
})
export class UserDetailsDialogComponent implements OnInit {
  @Input() mode!: string;
  @Input() userEmail: string = ""
  @Output() switchForm = new EventEmitter();
  disabled!: boolean;

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    phoneFormControl: ['phoneNumber', [Validators.required]],
    nameFormControl: ['name', [Validators.required]],
    lastNameFormControl: ['lastName', [Validators.required]],
    cityFormControl: ['city', [Validators.required]],
  });

  email = "";
  phoneNumber = "";
  name = "";
  lastName = "";
  city = "";

  hide = true;
  hide2 = true;

  constructor(
    @Inject(MatSnackBar) private _snackBar: MatSnackBar,
    private _formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private dialogRef: MatDialogRef<UserDetailsDialogComponent>
) {
  }

  ngOnInit(): void {
    this.disabled = this.mode === "edit"
    // this.formGroup.controls.emailFormControl.setValue(this.userEmail)
    console.log(this.mode)
    if (this.mode === "edit") {
      this.userService.getPropertyOwner(this.userEmail).subscribe((user) => {
        this.email = user.email
        this.name = user.firstName
        this.lastName = user.lastName
        this.phoneNumber = user.phoneNumber
        this.city = user.city
      })
    }
  }

  registerNewUser() {
    const requestBody: CreateUserCredentials = {
      "email": this.email,
      "firstName": this.name,
      "lastName": this.lastName,
      "phoneNumber": this.phoneNumber,
      "city": this.city,
    }
    console.log(requestBody)
    this.authService.createUser(requestBody).subscribe({
      next: () => {
        this.openSnackBar("We sent the user verification link")
        this.dialogRef.close();
      },
      error: (message) => this.openSnackBar(message)
    });
  }

  editUser() {
    const requestBody: CreateUserCredentials = {
      "email": this.email,
      "firstName": this.name,
      "lastName": this.lastName,
      "phoneNumber": this.phoneNumber,
      "city": this.city,
    }
    console.log(requestBody)
    this.userService.editPropertyOwner(requestBody).subscribe({
      next: () => {
        this.openSnackBar("Values successfully updated")
        this.dialogRef.close();
      },
      error: (message) => this.openSnackBar(message)
    });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

}
