import {Component, Input, OnInit} from '@angular/core';
import {BasicObjectDetails} from "../../../../model/Object";

@Component({
  selector: 'app-object-card',
  templateUrl: './object-card.component.html',
  styleUrls: ['./object-card.component.css']
})
export class ObjectCardComponent implements OnInit {

  @Input() object!: BasicObjectDetails;

  constructor() {
  }

  ngOnInit() {

  }

  objectDetails(id: string) {
    // TODO
  }
}
