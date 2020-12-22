import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { InventoryService } from 'src/app/shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SharedService } from 'src/app/shared/shared.service';

@Component({
  selector: 'app-customer-order-details',
  templateUrl: './customer-order-details.component.html',
  styleUrls: ['./customer-order-details.component.scss']
})
export class CustomerOrderDetailsComponent implements OnInit {

  role: any;
  userDetail;
  displayedColumns: string[] = ['sr_no', 'item', 'quantity', 'price', 'amount'];
  dataSource = new MatTableDataSource();
  dataSourceItems = new MatTableDataSource();
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  ELEMENT_DATA = [];

  constructor(private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private inventoryService: InventoryService,
    private dialogRef: MatDialogRef<CustomerOrderDetailsComponent>,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService
    ) {

  }

  ngOnInit(): void {
    this.userDetail = JSON.parse(window.sessionStorage.getItem('user'));
    this.role = sessionStorage.getItem('userRole');
    this.getOrderDetailsByOrderId();
  }

  getOrderDetailsByOrderId() {
    this.spinnerService.show();
    this.data.status = this.sharedService.getFormatedStatus(this.data.status);

    this.inventoryService.getOrderDetailsByOrderId(this.data.orderNo).subscribe((res: any) => {
    
      this.ELEMENT_DATA = res.result.inventoryDatas;

      this.dataSourceItems.data = this.ELEMENT_DATA;
      this.dataSourceItems.paginator = this.paginator;
      this.dataSourceItems.sort = this.sort;

      this.spinnerService.hide();
    },
      error => {
        this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  updateOrderStatus(status: string) {
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
      status: status,
      inventoryDatas: this.ELEMENT_DATA,
      isOrderChanged: this.data.isOrderChanged
    };
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
}
