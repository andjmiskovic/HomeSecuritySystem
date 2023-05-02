import {Component} from '@angular/core';
import {BasicObjectDetails, ObjectType} from "../../../../model/Object";
import {ObjectsService} from "../../../../services/objects.service";
import {MatSelectChange} from "@angular/material/select";

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
    console.log("Evo me")
    this.getCards();
  }

  applySearchFilter($event: KeyboardEvent) {
    this.getCards();
  }

  getCards() {
    this.objectsService.getObjects(this.searchFilter, this.type).subscribe((res) => {
        console.log(res)
      }
    //   {
    //   next: (objects) => {
    //     this.objects = objects
    //     console.log(objects)
    //   },
    //   error: err => console.error(err)
    // }
    )
  }

  selectedFilterChange($event: MatSelectChange) {
    this.getCards();
  }
}
