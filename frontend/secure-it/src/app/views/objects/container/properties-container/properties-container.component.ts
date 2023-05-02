import {Component} from '@angular/core';
import {BasicPropertyDetails, PropertyType} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-properties-container',
  templateUrl: './properties-container.component.html',
  styleUrls: ['./properties-container.component.css']
})
export class PropertiesContainerComponent {
  searchFilter = "";
  type: PropertyType | undefined;
  properties: BasicPropertyDetails[] = [];
  public fileTypes = Object.values(PropertyType);

  constructor(private propertiesService: PropertyService) {
  }

  getCards() {
    this.propertiesService.getObjects(this.searchFilter, this.type).subscribe({
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
}
