import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomepageModule} from "./views/homepage/homepage.module";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {NotAuthorizedPageComponent} from "./views/403/not-authorized-page/not-authorized-page.component";
import {HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatSelectModule} from "@angular/material/select";
import {SharedModule} from './shared/shared.module'
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {DashboardModule} from "./views/dashboard/dashboard.module";

@NgModule({
  declarations: [
    AppComponent,
    NotFoundPageComponent,
    NotAuthorizedPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HomepageModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    SharedModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    FormsModule,
    DashboardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
