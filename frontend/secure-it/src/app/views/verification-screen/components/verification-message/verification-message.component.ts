import {Component, Inject, Input} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-verification-message',
  templateUrl: './verification-message.component.html',
  styleUrls: ['./verification-message.component.css']
})
export class VerificationMessageComponent {
  @Input() isInvalidVerificationCode!: boolean;

  constructor(
    @Inject(MatSnackBar) private _snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private _formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      let verificationCode = params['code'] || "";
      if (!this.isInvalidVerificationCode)
        this.authService.verify(verificationCode).subscribe((res) => {
          console.log(res)
        })
    });
  }

  navigate() {
    this.router.navigate(['/']);
  }
}
