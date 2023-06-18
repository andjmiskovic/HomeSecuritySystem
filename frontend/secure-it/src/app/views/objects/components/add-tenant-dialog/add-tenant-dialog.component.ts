import {Component, Inject, Input} from '@angular/core';
import {
  CreatePropertyRequest,
  getKeyFromValue,
  getValueByKey,
  PropertyDetails,
  PropertyType, UpdatePropertyRequest
} from "../../../../model/Property";
import {FormBuilder, Validators} from "@angular/forms";
import {User} from "../../../../model/User";
import {MatDialogRef} from "@angular/material/dialog";
import {PropertyService} from "../../../../services/property.service";
import {AuthService} from "../../../../services/auth.service";
import {InviteUserToPropertyRequest} from "../../../../model/InviteUserToPropertyRequest";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-add-tenant-dialog',
  templateUrl: './add-tenant-dialog.component.html',
  styleUrls: ['./add-tenant-dialog.component.css']
})
export class AddTenantDialogComponent {
  @Input() property!: PropertyDetails;
  email: string = "";

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
  });

  constructor(
    // public dialogRef: MatDialogRef<PropertyEditFormDialogComponent>,
    private _formBuilder: FormBuilder,
    @Inject(MatSnackBar) private _snackBar: MatSnackBar,
    private propertyService: PropertyService) {
  }

  sendRequest() {
    const requestBody: InviteUserToPropertyRequest = {
      "propertyId": this.property.id,
      "userEmail": this.email,
    }
    console.log(requestBody)
    this.propertyService.sendInvitationToProperty(requestBody).subscribe({
      next: () => {
        this._snackBar.open("Successfully sent email to user", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      },
      error: (err) => {
        this._snackBar.open("There was an error while sending email", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
        console.error(err)
      }
    })
  }
}
