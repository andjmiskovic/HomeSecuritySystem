import {Component, Input, OnInit} from '@angular/core';
import {BasicPropertyDetails} from "../../../../model/Property";

@Component({
  selector: 'app-property-card',
  templateUrl: './property-card.component.html',
  styleUrls: ['./property-card.component.css']
})
export class PropertyCardComponent implements OnInit {

  @Input() property!: BasicPropertyDetails;

  constructor() {
  }

  ngOnInit() {
  }

  objectDetails(id: string) {
    // TODO
  }
}
