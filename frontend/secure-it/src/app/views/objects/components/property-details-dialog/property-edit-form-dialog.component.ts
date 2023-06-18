import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {PropertyService} from "../../../../services/property.service";
import {
  CreatePropertyRequest,
  getKeyFromValue, getValueByKey,
  PropertyDetails,
  PropertyType, UpdatePropertyRequest
} from "../../../../model/Property";
import {AuthService} from "../../../../services/auth.service";
import {User} from "../../../../model/User";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-property-details-dialog',
  templateUrl: './property-edit-form-dialog.component.html',
  styleUrls: ['./property-edit-form-dialog.component.css']
})
export class PropertyEditFormDialogComponent implements OnInit {
  @Input() mode!: string;

  formGroup = this._formBuilder.group({
    nameFormControl: ['name', [Validators.required]],
    addressFormControl: ['address', [Validators.required]],
  });
  propertyTypes: string[] = [];
  type: string = 'House';
  address!: string;
  name!: string;
  fileTypes = Object.values(PropertyType);
  loggedUser!: User
  image: string = ""
  property!: PropertyDetails;

  constructor(public dialogRef: MatDialogRef<PropertyEditFormDialogComponent>, private _formBuilder: FormBuilder, private propertyService: PropertyService, private authService: AuthService) {
  }

  ngOnInit(): void {
    if (this.mode === "edit") {
      this.type = getValueByKey(this.property.type)
      this.name = this.property.name
      this.address = this.property.address
    }
  }

  addNewProperty() {
    this.authService.getCurrentlyLoggedUser().subscribe((user) => {
      this.loggedUser = user
      const requestBody: CreatePropertyRequest = {
        "ownerId": this.loggedUser.id,
        "name": this.name,
        "address": this.address,
        "type": getKeyFromValue(this.type)!,
        "image": this.image
      }
      this.propertyService.addNewProperty(requestBody).subscribe(() => {
        this.dialogRef.close()
      })
    })
  }

  deleteProperty() {

  }

  editProperty() {
    this.authService.getCurrentlyLoggedUser().subscribe((user) => {
      this.loggedUser = user
      const requestBody: UpdatePropertyRequest = {
        "propertyId": this.property.id,
        "ownerId": this.loggedUser.id,
        "name": this.name,
        "address": this.address,
        "type": getKeyFromValue(this.type)!,
        "image": this.image
      }
      this.propertyService.editProperty(requestBody).subscribe(() => {
        this.dialogRef.close()
      })
    })
  }
}
