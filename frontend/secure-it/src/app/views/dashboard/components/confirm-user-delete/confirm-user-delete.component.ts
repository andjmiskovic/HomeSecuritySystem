import {Component, Inject, Input, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormBuilder} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {UserService} from "../../../../services/user.service";
import {MatDialogRef} from "@angular/material/dialog";
import {UserDetailsDialogComponent} from "../user-details-dialog/user-details-dialog.component";
import {PropertyService} from "../../../../services/property.service";
import {PropertyResponse} from "../../../../model/Property";

@Component({
  selector: 'app-confirm-user-delete',
  templateUrl: './confirm-user-delete.component.html',
  styleUrls: ['./confirm-user-delete.component.css']
})
export class ConfirmUserDeleteComponent implements OnInit{
  @Input() userEmail: string = ""
  @Input() parentDialog!: MatDialogRef<UserDetailsDialogComponent>;
  properties: PropertyResponse[] = []

  constructor(
    @Inject(MatSnackBar) private _snackBar: MatSnackBar,
    private _formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private propertyService: PropertyService,
    private dialogRef: MatDialogRef<ConfirmUserDeleteComponent>,
  ) {
  }

  cancelDelete() {
    this.dialogRef.close();
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

  deleteUser() {
    this.userService.deleteUser(this.userEmail).subscribe({
      next: () => {
        this.openSnackBar("Successfully deleted user")
        this.dialogRef.close();
        this.parentDialog.close()
      },
      error: (message) => this.openSnackBar(message)
    });
  }

  ngOnInit(): void {
    this.propertyService.getPropertiesOfOwner(this.userEmail).subscribe((res) => {
      this.properties = res
    })
  }
}
