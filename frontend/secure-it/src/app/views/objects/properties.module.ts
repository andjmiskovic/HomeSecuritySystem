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
import {SharedModule} from "../../shared/shared.module";
import {MatSelectModule} from "@angular/material/select";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {PropertiesContainerComponent} from './container/properties-container/properties-container.component';
import {PropertyCardComponent} from './components/property-card/property-card.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { PropertyEditFormDialogComponent } from './components/property-details-dialog/property-edit-form-dialog.component';

@NgModule({
  declarations: [
    PropertiesContainerComponent,
    PropertyCardComponent,
    PropertyEditFormDialogComponent
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
        MatPaginatorModule,
        MatTooltipModule
    ],
  exports: [
    PropertiesContainerComponent
  ]
})
export class PropertiesModule {
}
