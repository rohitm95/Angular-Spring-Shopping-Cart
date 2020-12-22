import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerUploadComponent } from './customer-upload.component';

describe('CustomerUploadComponent', () => {
  let component: CustomerUploadComponent;
  let fixture: ComponentFixture<CustomerUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
