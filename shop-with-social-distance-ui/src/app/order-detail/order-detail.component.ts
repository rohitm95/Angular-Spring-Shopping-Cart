import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { InventoryService } from '../shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedService } from '../shared/shared.service';

interface DialogData {
  order_no: string;
  delivery_date: string;
  time_slot: string;
  customer_details: {
    mobileNumber: string;
  };
  status: string;
  amountPayable: number;
  store: {};
  inventoryDatas: Array<{}>;
  customer_name: string;
}

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent implements OnInit {

  role: any;
  userDetail;
  isClicked = false;
  dataSource = new MatTableDataSource();
  constructor(private spinnerService: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private inventoryService: InventoryService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<OrderDetailComponent>,
    private sharedService: SharedService) { }
  displayedColumns: string[] = ['sr_no', 'item', 'quantity', 'price', 'amount'];

  ngOnInit(): void {
    this.userDetail = JSON.parse(sessionStorage.getItem('user'));
    this.role = sessionStorage.getItem('userRole');
    this.getOrderDetails();
  }

  quantityDecrement(element, index) {
    const qty = element.noOfItemsOrderded - 1;
    this.dataSource.data[index]['noOfItemsOrderded'] = qty;
    this.data.amountPayable = this.data.amountPayable - element.price;
    this.data.isOrderChanged = true;
  }
  quantityIncrement(element, index) {
    const qty = element.noOfItemsOrderded + 1;
    this.dataSource.data[index]['noOfItemsOrderded'] = qty;
    this.data.amountPayable = this.data.amountPayable + element.price;
    this.data.isOrderChanged = true;
  }

  markAsDelivered() {
    const userRole = {
      id: this.userDetail.role.id,
      name: this.userDetail.role.name
    };

    const orderData = {
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
    this.isClicked = true;
    
    this.spinnerService.show();
    this.inventoryService.updateOrderStatus(orderData, userRole).subscribe(
      (res: any) => {
        if (res !== null) {
          if (res.status === 400) {
            this.toastr.error(res.statusText);
          } else {
            this.toastr.success('Status Successfully Changed!');
            this.dialogRef.close();
          }
        } else {
          this.toastr.error('Server not responding please login and try again');
          this.dialogRef.close();
        }
        this.spinnerService.hide();
      },
      error => {
        this.toastr.error('An error occured!');
        this.dialogRef.close();
        this.spinnerService.hide();
      });
  }

  getOrderDetails() {
    this.spinnerService.show();
    this.inventoryService.getOrderDetailsByOrderId(this.data.order_no).subscribe(
      (res: any) => {
        this.data.inventoryDatas = res.result.inventoryDatas;
        this.dataSource.data = this.data.inventoryDatas;
        this.spinnerService.hide();
      },
      error => {
        this.toastr.error('An error occured!');
        this.dialogRef.close();
        this.spinnerService.hide();
      });
  }
}
