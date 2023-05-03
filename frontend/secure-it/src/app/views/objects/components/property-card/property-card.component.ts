import {Component, Input, OnInit} from '@angular/core';
import {BasicPropertyDetails} from "../../../../model/Property";
import {MatDialog} from "@angular/material/dialog";
import {PropertyDetailsComponent} from "../property-details/property-details.component";

@Component({
  selector: 'app-property-card',
  templateUrl: './property-card.component.html',
  styleUrls: ['./property-card.component.css']
})
export class PropertyCardComponent implements OnInit {

  @Input() property!: BasicPropertyDetails;

  constructor(private dialog: MatDialog) {
  }

  ngOnInit() {
  }

  objectDetails(id: string) {
    let dialogRef = this.dialog.open(PropertyDetailsComponent);
    dialogRef.id = id;
  }
}
