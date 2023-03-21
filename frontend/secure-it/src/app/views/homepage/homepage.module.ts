import {NgModule} from "@angular/core";
import {LoginComponent} from "./components/login/login.component";
import {HomepageContainerComponent} from './container/homepage-container/homepage-container.component';
import {RegistrationComponent} from './components/registration/registration.component';

@NgModule({
  declarations: [
    HomepageContainerComponent,
    LoginComponent,
    RegistrationComponent
  ],
    imports: [
    ],
  exports: [
    HomepageContainerComponent,
    RegistrationComponent
  ],
  bootstrap: [HomepageContainerComponent]
})
export class HomepageModule {
}
