import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerificationMessageComponent } from './verification-message.component';

describe('VerificationMessageComponent', () => {
  let component: VerificationMessageComponent;
  let fixture: ComponentFixture<VerificationMessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerificationMessageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerificationMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
