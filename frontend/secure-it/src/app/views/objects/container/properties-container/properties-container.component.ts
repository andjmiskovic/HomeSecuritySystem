import {Component} from '@angular/core';
import {BasicPropertyDetails, getKeyFromValue, PropertyType} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {MatSelectChange} from "@angular/material/select";
import {MatDialog} from "@angular/material/dialog";
import {
  PropertyDetailsDialogComponent
} from "../../components/property-details-dialog/property-details-dialog.component";

@Component({
  selector: 'app-properties-container',
  templateUrl: './properties-container.component.html',
  styleUrls: ['./properties-container.component.css']
})
export class PropertiesContainerComponent {
  searchFilter = "";
  type: string | undefined = 'All';
  properties: BasicPropertyDetails[] = [];
  public fileTypes = Object.values(PropertyType);

  constructor(private propertiesService: PropertyService, private dialog: MatDialog) {
    this.getCards();
  }

  getCards() {
    console.log(this.searchFilter)
    console.log(this.type)
    let type = this.type
    if (type === "All") type = undefined
    this.propertiesService.getObjects(this.searchFilter, getKeyFromValue(type)).subscribe({
      next: (properties) => this.properties = properties,
      error: err => console.error(err)
    })
  }

  selectedFilterChange($event: MatSelectChange) {
    this.getCards();
  }

  applySearchFilter($event: KeyboardEvent) {
    this.getCards();
  }

  addNewProperty() {
    const dialogRef = this.dialog.open(PropertyDetailsDialogComponent);
    dialogRef.componentInstance.mode = 'create';
    dialogRef.afterClosed().subscribe(() => this.getCards())
  }
}
