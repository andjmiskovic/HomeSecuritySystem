import {Component} from '@angular/core';
import {BasicPropertyDetails, getKeyFromValue, PropertyType} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {MatSelectChange} from "@angular/material/select";
import {MatDialog} from "@angular/material/dialog";
import {
  PropertyEditFormDialogComponent
} from "../../components/property-details-dialog/property-edit-form-dialog.component";
import {AuthService} from "../../../../services/auth.service";

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
  userRole: string;

  constructor(private propertiesService: PropertyService, private dialog: MatDialog, private authService: AuthService) {
    this.userRole = localStorage.getItem("userRole") || ""
    this.getCards();
  }

  getCards() {
    console.log(this.searchFilter)
    console.log(this.type)
    let type = this.type
    if (type === "All") type = undefined
    if (this.userRole==="ROLE_ADMIN") {
      this.propertiesService.getProperties(this.searchFilter, getKeyFromValue(type)).subscribe({
        next: (properties) => this.properties = properties,
        error: err => console.error(err)
      })
    } else if (this.userRole === "ROLE_PROPERTY_OWNER") {
      this.authService.getCurrentlyLoggedUser().subscribe((user) => {
        this.propertiesService.getOwnersProperties(this.searchFilter, getKeyFromValue(type), user.id).subscribe({
          next: (properties) => this.properties = properties,
          error: err => console.error(err)
        })
      })
    }
  }

  selectedFilterChange($event: MatSelectChange) {
    this.getCards();
  }

  applySearchFilter($event: KeyboardEvent) {
    this.getCards();
  }

  addNewProperty() {
    const dialogRef = this.dialog.open(PropertyEditFormDialogComponent);
    dialogRef.componentInstance.mode = 'create';
    dialogRef.afterClosed().subscribe(() => this.getCards())
  }
}
