import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewPrivateKeyComponent } from './preview-private-key.component';

describe('PreviewPrivateKeyComponent', () => {
  let component: PreviewPrivateKeyComponent;
  let fixture: ComponentFixture<PreviewPrivateKeyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreviewPrivateKeyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreviewPrivateKeyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
