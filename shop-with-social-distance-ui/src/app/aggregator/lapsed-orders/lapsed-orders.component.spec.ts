import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LapsedOrdersComponent } from './lapsed-orders.component';

describe('LapsedOrdersComponent', () => {
  let component: LapsedOrdersComponent;
  let fixture: ComponentFixture<LapsedOrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LapsedOrdersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LapsedOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
