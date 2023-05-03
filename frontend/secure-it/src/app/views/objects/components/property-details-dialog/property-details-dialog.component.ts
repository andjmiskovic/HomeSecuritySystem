import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {PropertyService} from "../../../../services/property.service";

@Component({
  selector: 'app-property-details-dialog',
  templateUrl: './property-details-dialog.component.html',
  styleUrls: ['./property-details-dialog.component.css']
})
export class PropertyDetailsDialogComponent implements OnInit {
  @Input() mode!: string;

  formGroup = this._formBuilder.group({
    nameFormControl: ['name', [Validators.required]],
    addressFormControl: ['address', [Validators.required]],
    cityFormControl: ['city', [Validators.required]],
  });
  propertyTypes: string[] = [];
  type: string = '';
  address!: string;
  name!: string;
  city!: string;

  constructor(private _formBuilder: FormBuilder, private propertyService: PropertyService) {
  }

  ngOnInit(): void {
    this.propertyService.getPropertyTypes().subscribe((res) => {
      this.propertyTypes = res
    })
  }

  addNewProperty() {

  }

  deleteProperty() {

  }

  editProperty() {

  }
}
