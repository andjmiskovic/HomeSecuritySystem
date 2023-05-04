import {Component, Input, OnInit} from '@angular/core';
import {PropertyDetails} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {PropertyEditFormDialogComponent} from "../property-details-dialog/property-edit-form-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-property-details',
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.css']
})
export class PropertyDetailsComponent implements OnInit {
  @Input() id!: string;
  property: PropertyDetails = new PropertyDetails();

  constructor(private propertyService: PropertyService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadProperty();
  }

  loadProperty() {
    this.propertyService.getProperty(this.id).subscribe({
      next: (p) => {
        this.property = p;
        console.log(this.property)
      },
      error: err => console.error(err)
    });
  }

  setOwner(id: string) {
    this.propertyService.setOwner(this.id, id).subscribe({
      next: () => this.loadProperty(),
      error: err => console.error(err)
    });
  }

  removeFromProperty(id: string) {
    this.propertyService.removeTenant(this.id, id).subscribe({
      next: () => this.loadProperty(),
      error: err => console.error(err)
    });
  }

  editProperty() {
    const dialogRef = this.dialog.open(PropertyEditFormDialogComponent);
    dialogRef.componentInstance.mode = 'edit';
    dialogRef.componentInstance.property = this.property;
    dialogRef.afterClosed().subscribe(() => this.loadProperty())
  }
}
