import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerAddUpdateComponent } from './customer-addupdate.component';

describe('CustomerAddupdateComponent', () => {
  let component: CustomerAddUpdateComponent;
  let fixture: ComponentFixture<CustomerAddUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerAddUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerAddUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
