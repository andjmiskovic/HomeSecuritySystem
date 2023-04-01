import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotAuthorizedPageComponent} from './views/403/not-authorized-page/not-authorized-page.component';
import {NotFoundPageComponent} from './views/404/not-found-page/not-found-page.component';
import {HomepageContainerComponent} from './views/homepage/container/homepage-container/homepage-container.component';
import {CertificateListComponent} from "./views/certificates/container/certificate-list/certificate-list.component";
import {
  DashboardContainerComponent
} from "./views/dashboard/container/dashboard-container/dashboard-container.component";
import {CsrListComponent} from "./views/csr/container/csr-list/csr-list.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'certificates', component: CertificateListComponent},
  {path: 'csrs', component: CsrListComponent},
  {path: 'dashboard', component: DashboardContainerComponent},
  {path: '403', component: NotAuthorizedPageComponent},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
