import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input() currentPage = 'dashboard';
  @Input() userRole = 'ADMIN';

  constructor(private router: Router) {
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }

  logout() {
  }
}
