import { Component, OnInit, ViewChild } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { InventoryService } from 'src/app/shared/inventory.service';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { CustomerOrderDetailsComponent } from './customer-order-details/customer-order-details.component';
import { SharedService } from 'src/app/shared/shared.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatInput } from '@angular/material/input';
import { MarkDeliveryDialogComponent } from 'src/app/admin/mark-delivery-dialog/mark-delivery-dialog.component';
import { OrderDetailComponent } from 'src/app/order-detail/order-detail.component';
import { CancelOrderDialogComponent } from 'src/app/cancel-order-dialog/cancel-order-dialog.component';

@Component({
  selector: 'app-customer-order-list',
  templateUrl: './customer-order-list.component.html',
  styleUrls: ['./customer-order-list.component.scss']
})
export class CustomerOrderListComponent implements OnInit {

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild('dateInput', { read: MatInput }) dateInput: MatInput;

  displayedColumns: string[] = [
    'orderNo',
    'deliveryDate',
    'time_slot',
    'amount',
    'status',
    'actions'];

  dataSource = new MatTableDataSource();
  dataSourceFiltered = new MatTableDataSource();
  ELEMENT_DATA = [];
  userFullName = "";
  orderNo = "";
  mobileNumber = "";
  deliveryDate = "";
  orderStatus = "";
  status = ["Ready", "Pending", "Completed", "Lapsed", "Cancelled"];
  filterType;
  tableFilters = [];

  constructor(private dialog: MatDialog,
    private spinnerService: NgxSpinnerService,
    private toastr: ToastrService,
    private router: Router,
    private inventoryService: InventoryService,
    private sharedService: SharedService) { }

  ngOnInit(): void {
    this.loadCustomerOrderData();
  }

  setFormatted_ELEMENT_DATA(orderResult) {
    this.ELEMENT_DATA = [];
    orderResult.forEach(element => {
      let status = this.sharedService.getFormatedStatus(element.status);
      let formattedDate = this.sharedService.getFormattedDate(element.deliveryDate);
      let formatedSlot = this.sharedService.getFormattedSlot(element.slotFrom, element.slotTo);

      let obj = {
        orderNo: element.orderNo,
        time_slot: formatedSlot,
        amount: element.amountPayable,
        delivery_date: formattedDate,
        customer: element.customer,
        order_status: status,
        amountPayable: element.amountPayable,
        store: element.store,
        inventoryDatas: element.inventoryDatas,
        isOrderChanged: element.isOrderChanged,
        customer_name: this.userFullName,
        cancelBy: element.cancelBy,
        cancelRemark: element.cancelRemark,
        deliveryDate: element.deliveryDate,
        order_no: element.order_no,
        slotFrom: element.slotFrom,
        slotTo: element.slotTo,
        status: element.status
      };

      this.ELEMENT_DATA.push(obj);
    });
  }

  loadCustomerOrderData() {
    this.spinnerService.show();
    let user = JSON.parse(window.sessionStorage.getItem('user'));
    this.userFullName = user.firstName + ' ' + user.lastName;
    this.inventoryService.getOrdersByCustomerId(user.id).subscribe(
      (res) => {
        this.setFormatted_ELEMENT_DATA(res.result);
        debugger;
        this.dataSource.data = this.ELEMENT_DATA;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        this.dataSourceFiltered.data = this.ELEMENT_DATA;
        this.dataSourceFiltered.paginator = this.paginator;
        this.dataSourceFiltered.sort = this.sort;

        this.dataSourceFiltered.filterPredicate =
          (data, filtersJson: string) => {
            const matchFilter = [];
            const filters = JSON.parse(filtersJson);
            filters.forEach(filter => {
              const val = data[filter.id] == null ? '' : data[filter.id].toString();
              let fval = filter.value;
              if (filter.id == 'delivery_date') {
                matchFilter.push(this.sharedService.compareDates(val, fval));
              }
              else {
                matchFilter.push(val.toLowerCase().includes(fval.toLowerCase()));
              }
            });
            return matchFilter.every(Boolean);
          };

        this.dataSource.filterPredicate =
          (data, filtersJson: string) => {
            const matchFilter = [];
            const filters = JSON.parse(filtersJson);
            filters.forEach(filter => {
              const val = data[filter.id] == null ? '' : data[filter.id].toString();
              let fval = filter.value;
              if (filter.id == 'deliveryDate') {
                matchFilter.push(this.sharedService.compareDates(val, fval));
              }
              else {
                matchFilter.push(val.toLowerCase().includes(fval.toLowerCase()));
              }
            });
            return matchFilter.every(Boolean);
          };

        this.spinnerService.hide();
      },
      error => {
        this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  refreshData() {
    this.loadCustomerOrderData();
    this.resetFilters();
  }

  applyFilter(filterValue: string, filter) {

    this.filterType = filter;

    if (this.filterType != 'deliveryDate') {
      filterValue = filterValue.trim().toLowerCase();
    }
    else{
      this.filterType = 'delivery_date';
    }

    if (this.filterType != "") {
      let filterIndex = this.tableFilters.findIndex(x => x.id === this.filterType);
      if (filterIndex > -1) {
        this.tableFilters[filterIndex].value = filterValue
      } else {
        this.tableFilters.push({
          id: this.filterType,
          value: filterValue
        });
      }
      this.dataSourceFiltered.filter = JSON.stringify(this.tableFilters);
      if (this.dataSourceFiltered.paginator) {
        this.dataSourceFiltered.paginator.firstPage();
      }
    }
    else {
      this.dataSourceFiltered.data = this.dataSource.data;
      this.dataSourceFiltered.paginator = this.dataSource.paginator;
    }
  }

  resetFilters() {
    this.orderNo = '';
    this.deliveryDate = '';
    this.orderStatus = '';
    this.dataSource.filter = '';
    this.dataSourceFiltered.filter = '';
    this.tableFilters = [];
  }

  openDialog(element) {
    const dialogRef = this.dialog.open(CustomerOrderDetailsComponent, {
      width: '70%',
      data: element
    }).afterClosed().subscribe(res => {
      this.refreshData();
    });
    }

    navigateTo(): void {
      this.sharedService.navigateToUsetLandingPageByRole();
    }
}
