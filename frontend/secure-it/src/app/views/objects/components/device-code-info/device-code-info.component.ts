import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-device-code-info',
  templateUrl: './device-code-info.component.html',
  styleUrls: ['./device-code-info.component.css']
})
export class DeviceCodeInfoComponent {
  @Input() code: string | undefined = undefined;
}
