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
import {EnterPasswordComponent} from "./components/enter-password/enter-password.component";
import {
  VerificationScreenContainerComponent
} from "./container/verification-screen-container/verification-screen-container.component";
import {VerificationMessageComponent} from "./components/verification-message/verification-message.component";
import { TenantInvitationContainerComponent } from './container/tenant-invitation-container/tenant-invitation-container.component';

@NgModule({
  declarations: [
    EnterPasswordComponent,
    VerificationScreenContainerComponent,
    VerificationMessageComponent,
    TenantInvitationContainerComponent,
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
    MatMenuModule
  ],
  exports: [
    EnterPasswordComponent,
    VerificationMessageComponent
  ],
  bootstrap: [VerificationScreenContainerComponent]
})
export class VerificationpageModule {
}
