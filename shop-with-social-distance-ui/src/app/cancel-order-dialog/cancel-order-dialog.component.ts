import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';
import { InventoryService } from '../shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-cancel-order-dialog',
  templateUrl: './cancel-order-dialog.component.html',
  styleUrls: ['./cancel-order-dialog.component.scss']
})
export class CancelOrderDialogComponent implements OnInit {

  cancelForm: FormGroup;
  isClicked = false;
  constructor(private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder, private inventoryService: InventoryService,
    private dialogRef: MatDialogRef<CancelOrderDialogComponent>,
    private spinnerService: NgxSpinnerService) {
    this.cancelForm = fb.group({
      reason: ['']
    });
  }

  ngOnInit(): void {
    console.log(this.data);
  }

  updateOrderStatus(formValue) {
    this.isClicked = true;
    let data = this.data.element;
    let obj = {
      amountPayable: data.amountPayable,
      cancelBy: data.cancelBy,
      cancelRemark: formValue.value.reason,
      customer: data.customer,
      deliveryDate: data.deliveryDate,
      inventoryDatas: data.inventoryDatas,
      order_no: data.order_no,
      slotFrom: data.slotFrom,
      slotTo: data.slotTo,
      status: data.status,
      store: data.store
    };
    obj.cancelRemark = formValue.value.reason;
    obj.customer.role.id = 1;
    obj.customer.role.name = 'Admin';
    this.spinnerService.show();
    this.inventoryService.cancelOrder(obj).subscribe(res => {
      this.toastr.success('Order Cancelled Successfully!');
      this.dialogRef.close();
      this.spinnerService.hide();
    },
      error => {
        console.log(error);
        if (error.status === 200) {
          this.toastr.success('Order Cancelled Successfully!');
          this.dialogRef.close();
        } else {
          this.toastr.error('An Error Ocurred, Try again!');
          this.dialogRef.close();
        }
        this.spinnerService.hide();
      });
  }

}
