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
    this.propertyService.getProperty(this.id).subscribe({
      next: (p) => this.property = p,
      error: err => console.error(err)
    });
  }

  ngOnInit() {
    // this.propertyService.getProperty(this.id).subscribe({
    //   next: (p) => this.property = p,
    //   error: err => console.error(err)
    // });
  }

}
