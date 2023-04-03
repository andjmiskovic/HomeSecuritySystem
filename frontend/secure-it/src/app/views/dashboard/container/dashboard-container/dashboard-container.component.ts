import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CsrFormComponent} from "../../components/csr-form/csr-form.component";

@Component({
  selector: 'app-dashboard-container',
  templateUrl: './dashboard-container.component.html',
  styleUrls: ['./dashboard-container.component.css']
})
export class DashboardContainerComponent {

  userRole: string

  constructor(public dialog: MatDialog) {
    this.userRole = localStorage.getItem("userRole") || ""
  }

  openNewCertificateDialog() {
    this.dialog.open(CsrFormComponent, {
      height: '500px',
      width: '600px',
    });
  }
}

