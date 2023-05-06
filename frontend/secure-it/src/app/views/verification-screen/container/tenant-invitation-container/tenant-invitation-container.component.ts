import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PropertyService} from "../../../../services/property.service";

@Component({
  selector: 'app-tenant-invitation-container',
  templateUrl: './tenant-invitation-container.component.html',
  styleUrls: ['./tenant-invitation-container.component.css']
})
export class TenantInvitationContainerComponent implements OnInit {

  title: string = ""
  subtitle: string = ""
  affirmative: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private propertyService: PropertyService
  ) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      let verificationCode = params['code'] || "";
      this.propertyService.verifyInvite(verificationCode).subscribe({
        next: () => {
          this.title = "Congratulations";
          this.subtitle = "You have successfully accepted invitation to property. Login to preview it!"
          this.affirmative = true
        },
        error: (res) => {
          console.error(res)
          this.title = "Unfortunately there was an error"
          this.subtitle = res.error.message
          this.affirmative = false
        }
      })
    });
  }

  navigate() {
    this.router.navigate(['/']);
  }
}
