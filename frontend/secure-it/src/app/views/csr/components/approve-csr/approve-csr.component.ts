import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrService} from "../../../../services/csr.service";

@Component({
  selector: 'app-approve-csr',
  templateUrl: './approve-csr.component.html',
  styleUrls: ['./approve-csr.component.css']
})
export class ApproveCsrComponent {
  @Input() id!: string;

  reason = "";
  commonReasons = [
    'Incomplete or inaccurate information',
    'Requester doesn\'t meet requirements',
    'Compromised account or suspicion of fraud',
    'Violation of policies or regulatory requirements',
    'Suspicion of malicious activities',
    'Insufficient or unreasonable justification'
  ];
  otherReason: boolean = false;

  constructor(public dialogRef: MatDialogRef<ApproveCsrComponent>, private csrService: CsrService) {
  }

  onNoClick() {
    this.dialogRef.close();
  }

  approve() {
    // this.csrService.issueCertificate(this.id, this.reason).subscribe({
    //   next: () => this.onNoClick(),
    //   error: (err) => console.error(err)
    // })
  }
}
