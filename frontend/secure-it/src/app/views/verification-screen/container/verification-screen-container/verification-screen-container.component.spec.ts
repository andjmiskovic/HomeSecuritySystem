import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerificationScreenContainerComponent } from './verification-screen-container.component';

describe('VerificationScreenContainerComponent', () => {
  let component: VerificationScreenContainerComponent;
  let fixture: ComponentFixture<VerificationScreenContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerificationScreenContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerificationScreenContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
