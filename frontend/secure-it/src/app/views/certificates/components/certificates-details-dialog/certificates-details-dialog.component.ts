import {Component, Input} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-certificates-details-dialog',
  templateUrl: './certificates-details-dialog.component.html',
  styleUrls: ['./certificates-details-dialog.component.css']
})
export class CertificatesDetailsDialogComponent {
  @Input() id!: number;
  @Input() dialogRef!: MatDialogRef<CertificatesDetailsDialogComponent>;

}
