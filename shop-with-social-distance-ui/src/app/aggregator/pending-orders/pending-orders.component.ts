import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { OrderDetailComponent } from '../../order-detail/order-detail.component';
import { InventoryService } from '../../shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { AppDateAdapter, APP_DATE_FORMATS } from '../../shared/format-datepicker';
import { ToastrService } from 'ngx-toastr';
import { SharedService } from 'src/app/shared/shared.service';

@Component({
  providers: [
    { provide: DateAdapter, useClass: AppDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS },
  ],
  selector: 'app-pending-orders',
  templateUrl: './pending-orders.component.html',
  styleUrls: ['./pending-orders.component.scss']
})
export class PendingOrdersComponent implements OnInit {

  constructor(private toastr: ToastrService,
    public dialog: MatDialog,
    private inventoryService: InventoryService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService) { }

  ELEMENT_DATA = [];
  displayedColumns: string[] = [
    'order_no',
    'customer_name',
    'time_slot',
    'delivery_date',
    'contact',
    'actions',
  ];
  dataSource = new MatTableDataSource();
  elementCount = 0;
  customerName = '';
  orderNo = '';
  deliveryDate = '';
  filterType;
  tableFilters = [];

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  ngOnInit(): void {
    this.getOrders();
  }

  openDialog(elem): void {
    const dialogRef = this.dialog.open(OrderDetailComponent, {
      width: '70%',
      data: elem
    }).afterClosed().subscribe(res => {
      this.getOrders();
    });
  }

  applyFilter(filterValue: string, filter, type) {
    if (filter === 'cust') {
      this.filterType = 'customer_name';
    }
    if (filter === 'order') {
      this.filterType = 'order_no';
    } else if (filter === 'time') {
      this.filterType = 'time_slot';
    }
    if (filter === 'date') {
      this.filterType = 'delivery_date';
    }
    let filterIndex = this.tableFilters.findIndex(x => x.id === this.filterType);
    if (filterIndex > -1) {
      this.tableFilters[filterIndex].value = filterValue.trim().toLowerCase();
    } else {
      this.tableFilters.push({
        id: this.filterType,
        value: filterValue.trim().toLowerCase()
      });
    }

    if (type === 'Pending') {  // filter of table according to the type
      this.dataSource.filter = JSON.stringify(this.tableFilters);
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
    this.elementCount = this.dataSource.filteredData.length;
  }

  getOrders() {
    this.spinnerService.show();
    this.inventoryService.getOrders('INITIATED').subscribe(
      (res) => {
        this.ELEMENT_DATA = [];
        res.result.forEach(element => {
          if (element.status === 'INITIATED') {
            this.ELEMENT_DATA.push(this.sharedService.getFormatedOrderObject(element));
            this.elementCount = this.ELEMENT_DATA.length;
          }
        });
        if(this.elementCount==0){
        this.dataSource.filteredData.length == 0;
        location.reload();}
        this.dataSource.data = this.ELEMENT_DATA;
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spinnerService.hide();
      }, (error) => {
        if (error.error.statusText == 'Data Not Found') {
          this.toastr.error(error.error.statusText);
        } else {
          this.toastr.error('Connection Error!');
        }
        this.spinnerService.hide();
      });

    this.dataSource.filterPredicate = (
      data, filtersJson: string
    ) => {
      const matchFilter = [];
      const filters = JSON.parse(filtersJson);

      filters.forEach((filter) => {
        let val: string = data[filter.id] === null ? '' : data[filter.id];
        val = val.toString();
        matchFilter.push(
          val.toLowerCase().includes(filter.value.toLowerCase())
        );
      });
      return matchFilter.every(Boolean);
    };
  }



  resetFilters() {
    this.customerName = '';
    this.orderNo = '';
    this.deliveryDate = '';
    this.tableFilters = [];
    this.dataSource.filter = '';
    this.elementCount = this.dataSource.filteredData.length;
  }

}
