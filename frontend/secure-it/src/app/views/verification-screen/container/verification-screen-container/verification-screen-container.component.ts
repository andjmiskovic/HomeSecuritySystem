import {Component, Inject} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../../services/auth.service";
import {FormBuilder} from "@angular/forms";
import {downloadTxtFile} from "../../../../utils/DownloadFile";

@Component({
  selector: 'app-verification-screen-container',
  templateUrl: './verification-screen-container.component.html',
  styleUrls: ['./verification-screen-container.component.css']
})
export class VerificationScreenContainerComponent {
  isPasswordSet!: boolean;
  isInvalidVerificationCode!: boolean;

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
      this.authService.getIsPasswordSet(verificationCode).subscribe({
          next: (isPasswordSet) => {
            console.log("EVE me")
            this.isPasswordSet = isPasswordSet
            this.isInvalidVerificationCode = false
            console.log(this.isPasswordSet)
            console.log(this.isInvalidVerificationCode)
          },
          error: (res) => {
            console.log("EVE me 2")
            this.isInvalidVerificationCode = true
          }
        }

        //   (isPasswordSet) => {
        //   console.log(isPasswordSet)
        //   console.log("response")
        //   this.isPasswordSet = isPasswordSet
        // }
      )
    });
  }
}
