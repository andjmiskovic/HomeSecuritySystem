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
import {DeviceContainerComponent} from "./container/device-container/device-container.component";
import {SharedModule} from "../../shared/shared.module";
import {MatTableModule} from "@angular/material/table";
import {MatSelectModule} from "@angular/material/select";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSortModule} from "@angular/material/sort";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatTabsModule} from "@angular/material/tabs";
import {AlarmsContainerComponent} from "./components/alarms-container/alarms-container.component";
import {EditAlarmsDialogComponent} from "./components/edit-alarms-dialog/edit-alarms-dialog.component";
import { LogsComponent } from './components/logs/logs.component';

@NgModule({
  declarations: [
    DeviceContainerComponent,
    AlarmsContainerComponent,
    EditAlarmsDialogComponent,
    LogsComponent
  ],
  imports: [
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
    MatTableModule,
    MatSelectModule,
    MatTooltipModule,
    MatSortModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTabsModule,
  ],
  exports: [
    DeviceContainerComponent
  ],
  bootstrap: [DeviceContainerComponent]
})
export class DeviceModule {
}
