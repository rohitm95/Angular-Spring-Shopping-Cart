import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { InventoryService } from '../../shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { OrderDetailComponent } from 'src/app/order-detail/order-detail.component';
import { UserService } from '../../shared/user.service';

@Component({
  selector: 'app-mark-delivery-dialog',
  templateUrl: './mark-delivery-dialog.component.html',
  styleUrls: ['./mark-delivery-dialog.component.scss']
})
export class MarkDeliveryDialogComponent implements OnInit {
  userDetail;
  constructor(private spinnerService: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public data: any, private inventoryService: InventoryService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<OrderDetailComponent>) { }

  ngOnInit(): void {
    this.userDetail = JSON.parse(sessionStorage.getItem('user'));
  }

  proceedForMarking(){
    let userRole = {
      id: this.userDetail.role.id,
      name: this.userDetail.role.name
    };

    let orderData = {
      order_no: this.data.orderNo,
      customer: this.data.customer,
      store: this.data.store,
      deliveryDate: this.data.deliveryDate,
      slotFrom: this.data.slotFrom,
      slotTo: this.data.slotTo,
      amountPayable: this.data.amountPayable,
      status: this.data.status,
      inventoryDatas: this.data.inventoryDatas,
      isOrderChanged: this.data.isOrderChanged
    };
    this.spinnerService.show();
    this.inventoryService.updateOrderStatus(orderData, userRole).subscribe(
      (res: any) => {
        if (res !== null) {
          this.toastr.success('Status Successfully Changed!');
          this.dialogRef.close();
        } else {
          this.toastr.error('Server not responding please login and try again');
          this.dialogRef.close();
        }
        console.log(res);
        this.spinnerService.hide();
      },
      error => {
        console.log(error);
        this.toastr.error('An error occured!');
        this.dialogRef.close();
        this.spinnerService.hide();
      });
  }

}
