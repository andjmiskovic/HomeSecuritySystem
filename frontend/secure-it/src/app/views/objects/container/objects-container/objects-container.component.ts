import {Component} from '@angular/core';
import {BasicObjectDetails, ObjectType} from "../../../../model/Object";
import {ObjectsService} from "../../../../services/objects.service";

@Component({
  selector: 'app-objects-container',
  templateUrl: './objects-container.component.html',
  styleUrls: ['./objects-container.component.css']
})
export class ObjectsContainerComponent {
  searchFilter = "";
  type: ObjectType | undefined;
  objects: BasicObjectDetails[] = [];

  constructor(private objectsService: ObjectsService) {
  }

  applySearchFilter($event: KeyboardEvent) {
    this.getCards();
  }

  getCards() {
    this.objectsService.getObjects(this.searchFilter, this.type).subscribe({
      next: (objects) => this.objects = objects,
      error: err => console.error(err)
    })
  }
}
