import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyDetailsDialogComponent } from './property-details-dialog.component';

describe('PropertyDetailsDialogComponent', () => {
  let component: PropertyDetailsDialogComponent;
  let fixture: ComponentFixture<PropertyDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertyDetailsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PropertyDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
