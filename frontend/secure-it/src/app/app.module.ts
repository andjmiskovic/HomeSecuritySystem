import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomepageModule} from "./views/homepage/homepage.module";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {NotAuthorizedPageComponent} from "./views/403/not-authorized-page/not-authorized-page.component";
import { HttpClientModule } from '@angular/common/http';
import {MatButtonModule} from "@angular/material/button";

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
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
