import {NgModule} from "@angular/core";
import {LoginComponent} from "./components/login/login.component";
import {HomepageContainerComponent} from './container/homepage-container/homepage-container.component';
import {RegistrationComponent} from './components/registration/registration.component';
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
import { VerificationCodeComponent } from './components/verification-code/verification-code.component';

@NgModule({
  declarations: [
    HomepageContainerComponent,
    LoginComponent,
    RegistrationComponent,
    VerificationCodeComponent,
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
    HomepageContainerComponent,
    RegistrationComponent
  ],
  bootstrap: [HomepageContainerComponent]
})
export class HomepageModule {
}
