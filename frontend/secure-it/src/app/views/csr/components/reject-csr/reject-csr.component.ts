import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {CsrService} from "../../../../services/csr.service";

@Component({
  selector: 'app-reject-csr',
  templateUrl: './reject-csr.component.html',
  styleUrls: ['./reject-csr.component.css']
})
export class RejectCsrComponent {
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

  constructor(public dialogRef: MatDialogRef<RejectCsrComponent>, private csrService: CsrService) {
  }

  onNoClick() {
    this.dialogRef.close();
  }

  reject() {
    this.csrService.rejectRequest(this.id, this.reason).subscribe({
      next: () => this.onNoClick(),
      error: (err) => console.error(err)
    })
  }
}
