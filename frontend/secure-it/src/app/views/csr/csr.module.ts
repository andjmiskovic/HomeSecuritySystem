import {NgModule} from "@angular/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {CsrDetailsDialogComponent} from './components/csr-details-dialog/csr-details-dialog.component';
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatTableModule} from "@angular/material/table";
import {SharedModule} from "../../shared/shared.module";
import {MatSelectModule} from "@angular/material/select";
import {MatOptionModule} from "@angular/material/core";
import {CertificateRequestsComponent} from './container/certificate-requests/certificate-requests.component';
import {RejectCsrComponent} from "./components/reject-csr/reject-csr.component";
import {ApproveCsrComponent} from "./components/approve-csr/approve-csr.component";

@NgModule({
  declarations: [
    CsrDetailsDialogComponent,
    CertificateRequestsComponent,
    RejectCsrComponent,
    ApproveCsrComponent
  ],
  imports: [
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    FormsModule,
    CommonModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatMenuModule,
    MatTableModule,
    MatSelectModule,
    MatOptionModule,
    SharedModule,
    MatPaginatorModule,
  ],
  exports: [],
  bootstrap: []
})
export class CsrModule {
}
