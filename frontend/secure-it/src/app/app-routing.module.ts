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
import {
  VerificationScreenContainerComponent
} from "./views/verification-screen/container/verification-screen-container/verification-screen-container.component";
import {
  PropertiesContainerComponent
} from "./views/objects/container/properties-container/properties-container.component";
import {
  TenantInvitationContainerComponent
} from "./views/verification-screen/container/tenant-invitation-container/tenant-invitation-container.component";
import {TokensContainerComponent} from "./views/tokens/container/tokens/tokens-container.component";
import {DeviceContainerComponent} from "./views/device/container/device-container/device-container.component";
import {LogsContainerComponent} from "./views/logs/container/logs-container/logs-container.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'certificates', component: CertificateListComponent, canActivate: [AuthGuard],data: {roles: ['ROLE_PROPERTY_OWNER','ROLE_ADMIN']}},
  {path: 'requests', component: CertificateRequestsComponent, canActivate: [AuthGuard],data: {roles: ['ROLE_PROPERTY_OWNER', 'ROLE_ADMIN']}},
  {path: 'dashboard', component: DashboardContainerComponent, canActivate: [AuthGuard],data: {roles: ['ROLE_PROPERTY_OWNER', 'ROLE_ADMIN']}},
  {path: 'properties', component: PropertiesContainerComponent, canActivate: [AuthGuard],data: {roles: ['ROLE_PROPERTY_OWNER', 'ROLE_ADMIN']}},
  {path: 'registration/verification', component: VerificationScreenContainerComponent},
  {path: 'invitation', component: TenantInvitationContainerComponent},
  {path: 'tokens', component: TokensContainerComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_ADMIN']}},
  {path: 'logs', component: LogsContainerComponent, canActivate: [AuthGuard], data: {roles: ['ROLE_ADMIN']}},
  {path: 'device/:id', component: DeviceContainerComponent},
  {path: '403', component: NotAuthorizedPageComponent},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
