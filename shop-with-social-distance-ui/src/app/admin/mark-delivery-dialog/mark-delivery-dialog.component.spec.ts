import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarkDeliveryDialogComponent } from './mark-delivery-dialog.component';

describe('MarkDeliveryDialogComponent', () => {
  let component: MarkDeliveryDialogComponent;
  let fixture: ComponentFixture<MarkDeliveryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarkDeliveryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarkDeliveryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
