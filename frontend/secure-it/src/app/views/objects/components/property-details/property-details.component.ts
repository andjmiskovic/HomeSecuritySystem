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
    this.propertyService.getProperty(id);
  }

}
