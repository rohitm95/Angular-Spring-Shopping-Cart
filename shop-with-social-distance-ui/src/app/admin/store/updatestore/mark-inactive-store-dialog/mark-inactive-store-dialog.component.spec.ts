import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarkInactiveStoreDialogComponent } from './mark-inactive-store-dialog.component';

describe('MarkInactiveStoreDialogComponent', () => {
  let component: MarkInactiveStoreDialogComponent;
  let fixture: ComponentFixture<MarkInactiveStoreDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarkInactiveStoreDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarkInactiveStoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
