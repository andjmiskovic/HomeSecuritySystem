import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-verification-screen',
  templateUrl: './verification-screen.component.html',
  styleUrls: ['./verification-screen.component.css']
})
export class VerificationScreenComponent implements OnInit {

  constructor(private route: ActivatedRoute, private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.authService.verify(params['code'] || "").subscribe((res) => {
        console.log(res)
      })
    });
  }

  navigate() {
    this.router.navigate(['/']);
  }
}
