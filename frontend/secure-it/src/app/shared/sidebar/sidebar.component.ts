import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input() currentPage = 'dashboard';
  userRole = 'ROLE_ADMIN';

  constructor(private router: Router, private authService: AuthService) {
    this.userRole = localStorage.getItem("userRole")!;
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        localStorage.removeItem('userRole');
        this.router.navigate(['']);
      },
      error: (err) => console.error(err)
    })
  }
}
