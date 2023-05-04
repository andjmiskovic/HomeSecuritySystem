import {Component, Input, OnInit} from '@angular/core';
import {PropertyDetails} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";

@Component({
  selector: 'app-property-details',
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.css']
})
export class PropertyDetailsComponent implements OnInit {
  @Input() id!: string;
  property: PropertyDetails = new PropertyDetails();

  constructor(private propertyService: PropertyService) {
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
}
