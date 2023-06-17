import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PropertyDetails} from "../../../../model/Property";
import {PropertyService} from "../../../../services/property.service";
import {PropertyEditFormDialogComponent} from "../property-details-dialog/property-edit-form-dialog.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AuthService} from "../../../../services/auth.service";
import {User} from "../../../../model/User";
import {AddTenantDialogComponent} from "../add-tenant-dialog/add-tenant-dialog.component";
import {DeviceManagementService} from "../../../../services/device.service";
import {DevicePairingInitRequest} from "../../../../model/Device";
import {Router} from "@angular/router";

@Component({
  selector: 'app-property-details',
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.css']
})
export class PropertyDetailsComponent implements OnInit {
  @Input() id!: string;
  property: PropertyDetails = new PropertyDetails();
  loggedUser!: User;
  userRole!: string;
  generatedCode: EventEmitter<string> = new EventEmitter<string>();

  constructor(private authService: AuthService, private propertyService: PropertyService, private dialog: MatDialog,
              private dialogRef: MatDialogRef<PropertyDetailsComponent>, private deviceService: DeviceManagementService,
              private router: Router) {
    this.userRole = localStorage.getItem("userRole") || ""
  }

  ngOnInit() {
    this.authService.getCurrentlyLoggedUser().subscribe((user) => {
      this.loggedUser = user;
      this.loadProperty();
    });
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

  editProperty() {
    const dialogRef = this.dialog.open(PropertyEditFormDialogComponent);
    dialogRef.componentInstance.mode = 'edit';
    dialogRef.componentInstance.property = this.property;
    dialogRef.afterClosed().subscribe(() => this.loadProperty())
  }

  deleteProperty() {
    this.propertyService.deleteProperty(this.property.id).subscribe(() => {
      this.dialogRef.close()
    })
  }

  openAddTenantDialog() {
    let dialogRef = this.dialog.open(AddTenantDialogComponent);
    dialogRef.componentInstance.property = this.property;
    // dialogRef.afterClosed().subscribe(() => this.updateDisplay.emit())
  }

  addNewDevice() {
    let reguest = new DevicePairingInitRequest();
    reguest.propertyId = this.property.id;
    this.deviceService.initializePairing(reguest).subscribe({
      next: value => {
        console.log(value)
        this.generatedCode.emit(value.code)
      }
    })
  }

  owner() {
    return this.loggedUser && this.property && this.property.owner && this.loggedUser.id === this.property.owner.id;
  }

  deviceDetails(id: string) {
    this.router.navigate(['/device/' + id]);
  }
}
