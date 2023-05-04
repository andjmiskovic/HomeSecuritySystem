import {Component, Input, OnInit} from '@angular/core';
import {BasicPropertyDetails} from "../../../../model/Property";
import {MatDialog} from "@angular/material/dialog";
import {PropertyDetailsComponent} from "../property-details/property-details.component";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-property-card',
  templateUrl: './property-card.component.html',
  styleUrls: ['./property-card.component.css']
})
export class PropertyCardComponent implements OnInit {

  @Input() property!: BasicPropertyDetails;
  loggedUserId!: string
  userRole!: string

  constructor(private dialog: MatDialog, private authService: AuthService) {
    this.userRole = localStorage.getItem("userRole") || ""
  }

  ngOnInit() {
    this.authService.getCurrentlyLoggedUser().subscribe((user) => {
      this.loggedUserId = user.id;
    })
  }

  objectDetails(id: string) {
    let dialogRef = this.dialog.open(PropertyDetailsComponent, {
      height: '650px',
      width: '800px'
    });
    dialogRef.componentInstance.id = id;
  }
}
