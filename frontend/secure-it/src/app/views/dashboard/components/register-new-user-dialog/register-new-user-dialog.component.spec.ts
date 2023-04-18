import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterNewUserDialogComponent } from './register-new-user-dialog.component';

describe('RegisterNewUserDialogComponent', () => {
  let component: RegisterNewUserDialogComponent;
  let fixture: ComponentFixture<RegisterNewUserDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterNewUserDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterNewUserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
