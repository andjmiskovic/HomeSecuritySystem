import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotAuthorizedPageComponent} from './views/403/not-authorized-page/not-authorized-page.component';
import {NotFoundPageComponent} from './views/404/not-found-page/not-found-page.component';
import {HomepageContainerComponent} from './views/homepage/container/homepage-container/homepage-container.component';
import {CertificateListComponent} from "./views/certificates/container/certificate-list/certificate-list.component";
import {
  DashboardContainerComponent
} from "./views/dashboard/container/dashboard-container/dashboard-container.component";
import {CertificateRequestsComponent} from "./views/csr/container/certificate-requests/certificate-requests.component";
import {AuthGuard} from "./model/AuthGuard";
import {VerificationCodeComponent} from "./views/homepage/components/verification-code/verification-code.component";
import {VerificationScreenComponent} from "./views/verification-screen/verification-screen.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'certificates', component: CertificateListComponent, canActivate: [AuthGuard]},
  {path: 'requests', component: CertificateRequestsComponent, canActivate: [AuthGuard]},
  {path: 'dashboard', component: DashboardContainerComponent, canActivate: [AuthGuard]},
  // {path: 'registration/verification?code=/:verificationCode', component: VerificationScreenComponent},
  {path: 'registration/verification', component: VerificationScreenComponent},
  {path: '403', component: NotAuthorizedPageComponent},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
