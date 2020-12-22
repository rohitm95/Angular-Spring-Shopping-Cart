import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { InventoryService } from 'src/app/shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-inventory-delete',
  templateUrl: './inventory-delete.component.html',
  styleUrls: ['./inventory-delete.component.scss']
})
export class InventoryDeleteComponent implements OnInit {

  isClicked = false;

  constructor(private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private inventoryService: InventoryService,
    private dialogRef: MatDialogRef<InventoryDeleteComponent>,
    private spinnerService: NgxSpinnerService) {

  }

  ngOnInit(): void {
  }

  deleteCustomer(customerId) {
    this.spinnerService.show();
    this.inventoryService.deleteInventoryData(customerId).subscribe(res => {
      this.toastr.success('Inventory Deleted Successfully!');
      this.dialogRef.close();
      this.spinnerService.hide();
    },
      error => {
        console.log(error);
        if (error.status === 200) {
          this.toastr.success('Inventory Deleted Successfully!');
          this.dialogRef.close();
        } else {
          this.toastr.error('An Error Ocurred, Please Try again!');
          this.dialogRef.close();
        }
        this.spinnerService.hide();
      });
  }
}
