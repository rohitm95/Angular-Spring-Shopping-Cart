import { Component, OnInit, ViewChild, Inject, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { AppDateAdapter, APP_DATE_FORMATS } from '../../shared/format-datepicker';
import { OrderDetailComponent } from '../../order-detail/order-detail.component';
import { InventoryService } from 'src/app/shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { CancelOrderDialogComponent } from 'src/app/cancel-order-dialog/cancel-order-dialog.component';
import { NgxSpinnerService } from 'ngx-spinner';
import { MarkDeliveryDialogComponent } from '../mark-delivery-dialog/mark-delivery-dialog.component';
import { SharedService } from 'src/app/shared/shared.service';

interface DialogData {
  email: string;
}

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
  providers: [
    { provide: DateAdapter, useClass: AppDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
  ]
})
export class OrdersComponent implements OnInit {
  public test: string;
  email: string;
  ELEMENT_DATA = [];
  displayedColumns: string[] = ['order_no', 'delivery_date', 'time_slot', 'customer_name', 'contact', 'amount', 'status', 'actions'];
  dataSource = new MatTableDataSource();
  customerName = '';
  orderNo = '';
  deliveryDate = '';
  deliveryStatus = '';
  elementCount = 0;
  filterType;
  tableFilters = [];
  status = ["Ready", "Pending","Completed","Lapsed","Cancelled"];
  original_date: any;

  constructor(private spinnerService: NgxSpinnerService,
    public dialog: MatDialog,
    private inventoryService: InventoryService,
    private toastr: ToastrService,
    private sharedService: SharedService) { }

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit() {
    this.getOrders();
  }

  @Input('orderStatus') set orderStatus(data){
    this.deliveryStatus = data;
  } 

  openDialog(elem) {
     const dialogRef = this.dialog.open(OrderDetailComponent, {
      width: '70%',
      data: elem
    }).afterClosed().subscribe(res => {
      this.getOrders();
    });
  }

  cancelOrder(element) {
    const dialogRef = this.dialog.open(CancelOrderDialogComponent, {
      data: { element }
    }).afterClosed().subscribe(res => {
      this.getOrders();
    });
  }

  getOrders() {
    this.spinnerService.show();
    this.inventoryService.getOrders().subscribe(
      (res) => {
        this.ELEMENT_DATA = [];
        res.result.forEach(element => {
         
            this.original_date = this.sharedService.getFormattedDate(element.deliveryDate);
          
            
            const customerName: string = element.customer.lastName === null || element.customer.lastName === ''
              ? element.customer.firstName
              : element.customer.firstName + ' ' + element.customer.lastName;

            let obj = {
              orderNo: element.order_no.toString(),
              delivery_date: this.original_date,
              time_slot: element.slotFrom + ' - ' + element.slotTo,
              customer: element.customer,
              order_status: this.sharedService.getFormatedStatus(element.status),
              amountPayable: element.amountPayable,
              store: element.store,
              inventoryDatas: element.inventoryDatas,
              isOrderChanged: element.isOrderChanged,
              customer_name: customerName,
              cancelBy: element.cancelBy,
              cancelRemark: element.cancelRemark,
              deliveryDate: element.deliveryDate,
              order_no: element.order_no,
              slotFrom: element.slotFrom,
              slotTo: element.slotTo,
              status: element.status
            };
            this.ELEMENT_DATA.push(obj);
            this.elementCount = this.ELEMENT_DATA.length;
         
        });
        this.dataSource.data = this.ELEMENT_DATA;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.spinnerService.hide();
        if(this.deliveryStatus) {
          this.applyFilter(this.deliveryStatus, 'status');
        }
      },
      error => {
        this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });

    this.dataSource.filterPredicate =
      (data, filtersJson: string) => {
        const matchFilter = [];
        const filters = JSON.parse(filtersJson);
        filters.forEach(filter => {
          const val = data[filter.id] == null ? '' : data[filter.id].toString();
          matchFilter.push(val.toLowerCase().includes(filter.value.toLowerCase()));
        });
        return matchFilter.every(Boolean);
      };
  }

  applyFilter(filterValue: string, filter) {
    if (filter === 'cust') {
      this.filterType = 'customer_name';
    }
    else if (filter === 'order') {
      this.filterType = 'order_no';
    }
    // else if (filter == 'time') {
    //   this.filterType = 'time_slot'
    // }
    else if (filter === 'date') {
      this.filterType = 'delivery_date';
    }
    else if (filter === 'status') {
      this.filterType = 'order_status';
    }

    let filterIndex = this.tableFilters.findIndex(x => x.id === this.filterType);
    if (filterIndex > -1) {
      this.tableFilters[filterIndex].value = filterValue.trim().toLowerCase()
    } else {
      this.tableFilters.push({
        id: this.filterType,
        value: filterValue.trim().toLowerCase()
      });
    }

    this.dataSource.filter = JSON.stringify(this.tableFilters);
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.elementCount = this.dataSource.filteredData.length;
  }

  resetFilters(){
    this.customerName = '';
    this.orderNo = '';
    this.deliveryDate = '';
    this.deliveryStatus = '';
    this.dataSource.filter = '';
    this.tableFilters = [];
    this.elementCount = this.dataSource.filteredData.length;
  }

  askForConfirmation(elem) {
    const dialogRef = this.dialog.open(MarkDeliveryDialogComponent, {
      width: '40%',
      data: elem
    }).afterClosed().subscribe(res => {
      this.getOrders();
    });
  }

}
