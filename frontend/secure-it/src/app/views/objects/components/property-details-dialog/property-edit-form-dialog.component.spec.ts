import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyEditFormDialogComponent } from './property-edit-form-dialog.component';

describe('PropertyDetailsDialogComponent', () => {
  let component: PropertyEditFormDialogComponent;
  let fixture: ComponentFixture<PropertyEditFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PropertyEditFormDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PropertyEditFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
