import {Component, OnInit, ViewChild} from '@angular/core';
import {DeviceDetailsResponse} from "../../../../model/Device";
import {ActivatedRoute} from "@angular/router";
import {DeviceManagementService} from "../../../../services/device.service";
import {MatDialog} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {AlarmsContainerComponent} from "../../components/alarms-container/alarms-container.component";

@Component({
  selector: 'app-device-container',
  templateUrl: './device-container.component.html',
  styleUrls: ['./device-container.component.css']
})
export class DeviceContainerComponent implements OnInit {
  @ViewChild(AlarmsContainerComponent) alarmsTableRef!: AlarmsContainerComponent;

  device = new DeviceDetailsResponse();
  deviceId: string | null = "";
  campaignOne = new FormGroup({
    start: new FormControl(new Date),
    end: new FormControl(new Date),
  });

  constructor(private router: ActivatedRoute, private deviceService: DeviceManagementService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.deviceId = this.router.snapshot.paramMap.get('id');
    this.deviceService.getDevice(this.deviceId).subscribe({
      next: value => {
        this.device = value;
        this.alarmsTableRef.loadAlarms(this.device)
      }
    })
  }

  downloadReport() {
    const renderPdf = (content: any) => {
      console.log(content)
      const element = document.createElement('a');
      const file = new Blob([content], {type: 'application/pdf'});
      element.href = URL.createObjectURL(file);
      element.download = "report.pdf";
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    }

    this.deviceService.getReport(this.campaignOne.value.start, this.campaignOne.value.end, this.deviceId).subscribe({
      next: (content) => {
        renderPdf(content)
      }, error: err => {
        console.log(err)
        renderPdf(err)
      }
    })
  }
}
