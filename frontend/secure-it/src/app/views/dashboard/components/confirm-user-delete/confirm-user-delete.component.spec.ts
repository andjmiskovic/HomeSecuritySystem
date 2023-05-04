import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmUserDeleteComponent } from './confirm-user-delete.component';

describe('ConfirmUserDeleteComponent', () => {
  let component: ConfirmUserDeleteComponent;
  let fixture: ComponentFixture<ConfirmUserDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmUserDeleteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmUserDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
