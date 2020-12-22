import { HttpErrorResponse } from '@angular/common/http';
import { Store } from 'src/app/models/Store';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { StoreService } from 'src/app/shared/store.service';

@Component({
  selector: 'app-mark-inactive-store-dialog',
  templateUrl: './mark-inactive-store-dialog.component.html',
  styleUrls: ['./mark-inactive-store-dialog.component.scss']
})
export class MarkInactiveStoreDialogComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Store,
    private toastr: ToastrService,
    private storeService: StoreService,
    private dialogRef: MatDialogRef<MarkInactiveStoreDialogComponent>
  ) { }

  ngOnInit(): void {
  }

  proceedForMarking() {
    this.storeService.changeActiveStatus(this.data.id, false).subscribe(
      (response: any) => {
        if (response.statusText === 'SUCCESS') {
          this.data.active = false;
          this.toastr.success('Status Changed Successfully');
          this.dialogRef.close();
        } else {
          this.toastr.error(response.statusText);
        }
      },
      (error: HttpErrorResponse) => {
        this.toastr.error('An Error Ocurred, Try again!');
      }
    );
  }

}
