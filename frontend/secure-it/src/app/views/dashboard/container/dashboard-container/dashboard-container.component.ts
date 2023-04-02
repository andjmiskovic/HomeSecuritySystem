import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CsrFormComponent} from "../../components/csr-form/csr-form.component";

@Component({
  selector: 'app-dashboard-container',
  templateUrl: './dashboard-container.component.html',
  styleUrls: ['./dashboard-container.component.css']
})
export class DashboardContainerComponent {

  constructor(public dialog: MatDialog) {
  }

  openNewCertificateDialog() {
    this.dialog.open(CsrFormComponent, {
      height: '450px',
      width: '600px',
    });
  }
}

