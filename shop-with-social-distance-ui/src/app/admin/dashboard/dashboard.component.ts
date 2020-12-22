import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { InventoryService } from 'src/app/shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OrderDetailComponent } from 'src/app/order-detail/order-detail.component';
import { SharedService } from 'src/app/shared/shared.service';
import { MatInput } from '@angular/material/input';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})


export class DashboardComponent implements OnInit {
  today = new Date();
  orderDetails: any = [];
  readyordersArr = [];
  readyorderslen = 0;
  pendingordersArr = [];
  pendingorderslen = 0;
  completedordersArr = [];
  completedorderlen = 0;
  lapsedordersArr = [];
  lapsedoredrlen = 0;
  cancelordersArr = [];
  cancelorderlen = 0;
  lessStock = [];
  outStock = [];
  totalStock = 0;

  @ViewChild('dateInput', { read: MatInput }) dateInput: MatInput;
  @Input() selectedDashBoardTabIndex
  @Output() orderStatusFromDashBoard = new EventEmitter<string>();

  constructor(private inventoryService: InventoryService,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    public dialog: MatDialog,
    private sharedService: SharedService) { }

  ngOnInit() {
    this.updateDashboard(null);
  }

  clearOrderArrays() {
    this.readyordersArr = [];
    this.pendingordersArr = [];
    this.completedordersArr = [];
    this.lapsedordersArr = [];
    this.cancelordersArr = [];
  }

  pushValuesInOrderArrays(orderDetails) {
    orderDetails.forEach(element => {
      // console.log(element.status);
      if (element.status === 'READY_TO_DELIVER') {
        this.readyordersArr.push(element);
      } else if (element.status === 'INITIATED') {
        this.pendingordersArr.push(element);
      } else if (element.status === 'COMPLETED') {
        this.completedordersArr.push(element);
      } else if (element.status === 'LAPSED') {
        this.lapsedordersArr.push(element);
      } else if (element.status === 'CANCEL_BY_ADMIN' || element.status === 'CANCEL_BY_AGGREGATOR') {
        this.cancelordersArr.push(element);
      }
    });
  }

  updateDashboardByDate(date) {
    this.inventoryService.getOrderByDate(date).subscribe((res: any) => {
      this.clearOrderArrays();
      // console.log(res.result);
      this.orderDetails = res.result;
      this.pushValuesInOrderArrays(this.orderDetails);
      this.readyorderslen = this.readyordersArr.length;
      this.pendingorderslen = this.pendingordersArr.length;
      this.completedorderlen = this.completedordersArr.length;
      this.lapsedoredrlen = this.lapsedordersArr.length;
      this.cancelorderlen = this.cancelordersArr.length;
      this.totalStock = this.readyordersArr.length + this.pendingordersArr.length +
        this.completedordersArr.length + this.lapsedordersArr.length + this.cancelordersArr.length;
      this.spinnerService.hide();
    },
      error => {
        //this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  updateDashboardWithoutDate() {
    this.spinnerService.show();
    this.inventoryService.getOrders().subscribe((res: any) => {
      this.clearOrderArrays();
      // console.log(res.result);
      this.orderDetails = res.result;
      this.pushValuesInOrderArrays(this.orderDetails);

      this.readyorderslen = this.readyordersArr.length;
      this.pendingorderslen = this.pendingordersArr.length;
      this.completedorderlen = this.completedordersArr.length;
      this.lapsedoredrlen = this.lapsedordersArr.length;
      this.cancelorderlen = this.cancelordersArr.length;
      this.totalStock = this.readyordersArr.length + this.pendingordersArr.length +
        this.completedordersArr.length + this.lapsedordersArr.length + this.cancelordersArr.length;
      this.spinnerService.hide();
    },
      error => {
        //this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  updateInventoryItems() {
    this.spinnerService.show();
    this.inventoryService.getInventoryItems(null).subscribe(
      (res: any) => {
        this.lessStock = [];
        this.outStock = [];
        res.result.forEach(element => {
          if (element.stock === 0) {
            this.outStock.push(element);
          }
          else if (element.stock < 5 && element.stock > 0) {
            this.lessStock.push(element);
          }          
        });
        this.spinnerService.hide();
      },
      error => {
        //this.toastr.error('Connection error!');
        this.spinnerService.hide();
      }
    );
  }

  updateDashboard(date) {
    this.updateInventoryItems();
    this.orderDetails = [];
    if (date == null) {
      this.updateDashboardWithoutDate();
    }
    else {
      this.updateDashboardByDate(date);
    }
  }

  changeDate(event: MatDatepickerInputEvent<Date>) {
    let time = new Date(event.value).getTime();
    time = time - new Date().getTimezoneOffset() * 60 * 1000;
    // this.today = new Date(event.value);
    this.updateDashboard(time);
  }

  clearDate() {
    this.dateInput.value = '';
    this.updateDashboard(null);
  }

  openOrderPopup(orderData): void {
    const dialogRef = this.dialog.open(OrderDetailComponent, {
      width: '70%',
      data: this.sharedService.getFormatedOrderObject(orderData)
    }).afterClosed().subscribe(res => {
      this.clearDate();
      this.updateDashboard(null);
    });
  }

  onClickViewAll(orderStatus): void {
    this.orderStatusFromDashBoard.emit(orderStatus);
    const tabGroup = this.selectedDashBoardTabIndex;
    tabGroup.selectedIndex = 4;
  }
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
