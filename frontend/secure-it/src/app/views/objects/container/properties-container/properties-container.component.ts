import {Component, OnInit, ViewChild} from '@angular/core';
import {PropertyResponse, getKeyFromValue, PropertyType} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {MatSelectChange} from "@angular/material/select";
import {MatDialog} from "@angular/material/dialog";
import {
  PropertyEditFormDialogComponent
} from "../../components/property-details-dialog/property-edit-form-dialog.component";
import {AuthService} from "../../../../services/auth.service";
import {HandshakeWebsocketShareService} from "../../../../services/handshake/handshake-websocketshare.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ApproveCsrComponent} from "../../../csr/components/approve-csr/approve-csr.component";
import {NotificationComponent} from "../../components/notification/notification.component";
import {PropertyDetailsComponent} from "../../components/property-details/property-details.component";
import {DeviceCodeInfoComponent} from "../../components/device-code-info/device-code-info.component";

@Component({
  selector: 'app-properties-container',
  templateUrl: './properties-container.component.html',
  styleUrls: ['./properties-container.component.css']
})
export class PropertiesContainerComponent implements OnInit {
  searchFilter = "";
  type: string | undefined = 'All';
  properties: PropertyResponse[] = [];
  public fileTypes = Object.values(PropertyType);
  userRole: string;
  code: string = ""

  constructor(private propertiesService: PropertyService, private dialog: MatDialog, private authService: AuthService,
              private websocketService: HandshakeWebsocketShareService,
  ) {
    this.userRole = localStorage.getItem("userRole") || "";
    this.getCards();
  }

  getCards() {
    let type = this.type;
    if (type === "All") {
      type = undefined;
    }
    if (this.userRole === "ROLE_ADMIN") {
      this.propertiesService.getProperties(this.searchFilter, getKeyFromValue(type)).subscribe({
        next: (properties) => this.properties = properties,
        error: err => console.error(err)
      })
    } else if (this.userRole === "ROLE_PROPERTY_OWNER") {
      this.authService.getCurrentlyLoggedUser().subscribe((user) => {
        this.propertiesService.getUsersProperties(this.searchFilter, getKeyFromValue(type), user.id).subscribe({
          next: (properties) => this.properties = properties,
          error: (err) => console.error(err)
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

  ngOnInit(): void {
    this.subscribeToNewNotifications();
  }

  private subscribeToNewNotifications(): void {
    this.websocketService.getNewValue().subscribe({
      next: (res: any) => {
        if (res) {
          let body = JSON.parse(res);
          const dialogRef = this.dialog.open(NotificationComponent);
          dialogRef.componentInstance.device = body;
          dialogRef.componentInstance.code = this.code
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  generatedCode(value: any) {
    console.log(value);
    this.code = value
    const dialogRef = this.dialog.open(DeviceCodeInfoComponent);
    dialogRef.componentInstance.code = this.code
  }
}
