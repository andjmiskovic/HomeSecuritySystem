import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomepageModule} from "./views/homepage/homepage.module";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {NotAuthorizedPageComponent} from "./views/403/not-authorized-page/not-authorized-page.component";
import {HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from "@angular/material/button";
import {CertificateListComponent} from './views/certificate-list/certificate-list.component';
import {MatIconModule} from "@angular/material/icon";
import {MatSelectModule} from "@angular/material/select";
import {SharedModule} from './shared/shared.module'
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";

@NgModule({
  declarations: [
    AppComponent,
    NotFoundPageComponent,
    NotAuthorizedPageComponent,
    CertificateListComponent,
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
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
