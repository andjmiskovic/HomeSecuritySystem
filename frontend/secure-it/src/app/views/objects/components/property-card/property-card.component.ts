import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PropertyResponse} from "../../../../model/Property";
import {MatDialog} from "@angular/material/dialog";
import {PropertyDetailsComponent} from "../property-details/property-details.component";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-property-card',
  templateUrl: './property-card.component.html',
  styleUrls: ['./property-card.component.css']
})
export class PropertyCardComponent implements OnInit {
  @Output() updateDisplay: EventEmitter<any> = new EventEmitter();
  @Input() property!: PropertyResponse;
  @Output() updateGeneratedCode: EventEmitter<string> = new EventEmitter<string>();
  loggedUserId!: string;
  userRole!: string;

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
    dialogRef.componentInstance.generatedCode.subscribe((value: string) => {
      this.updateGeneratedCode.emit(value)
    });
    dialogRef.afterClosed().subscribe(() => this.updateDisplay.emit())
  }
}
