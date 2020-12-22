import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryAddupdateComponent } from './inventory-addupdate.component';

describe('InventoryAddupdateComponent', () => {
  let component: InventoryAddupdateComponent;
  let fixture: ComponentFixture<InventoryAddupdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InventoryAddupdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InventoryAddupdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
