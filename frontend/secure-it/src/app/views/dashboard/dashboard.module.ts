import {NgModule} from "@angular/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatCardModule} from "@angular/material/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {CommonModule} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {DashboardContainerComponent} from './container/dashboard-container/dashboard-container.component';
import {SharedModule} from "../../shared/shared.module";
import {CsrFormComponent} from './components/csr-form/csr-form.component';
import {MatSelectModule} from "@angular/material/select";
import { UsersComponent } from './components/users/users.component';
import {MatTableModule} from "@angular/material/table";
import {MatTooltipModule} from "@angular/material/tooltip";
import { UserDetailsDialogComponent } from './components/user-details-dialog/user-details-dialog.component';
import { ConfirmUserDeleteComponent } from './components/confirm-user-delete/confirm-user-delete.component';

@NgModule({
  declarations: [
    DashboardContainerComponent,
    CsrFormComponent,
    UsersComponent,
    UserDetailsDialogComponent,
    ConfirmUserDeleteComponent,
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
    SharedModule,
    MatSelectModule,
    MatTableModule,
    MatTooltipModule
  ],
  exports: [
    DashboardContainerComponent
  ],
  bootstrap: [DashboardContainerComponent]
})
export class DashboardModule {
}
