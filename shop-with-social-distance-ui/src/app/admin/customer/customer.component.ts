import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { CustomerUploadComponent } from './customer-upload/customer-upload.component';
import { CustomerService } from '../../shared/customer.service';
import { CustomerDeleteComponent } from './customer-delete/customer-delete.component';
import { CustomerAddUpdateComponent } from './customer-addupdate/customer-addupdate.component';
import { MatSort } from '@angular/material/sort';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  displayedColumnsCustomer: string[] = [
    'firstName',
    'emailId',
    'mobileNumber',
    'isActive',
    'username',
    'category',
    'role',
    'afd_purchase_limit',
    'nonAFD_purchase_limit',
    'actions'];
  dataSourceCustomer = new MatTableDataSource();
  ELEMENT_DATA_CUSTOMER = [];
  customerName = "";
  mobileNumber = "";
  category = "";
  isActive = "";
  categories = ["A", "B", "C"];
  userStatus = ["Active", "Inactive"]

  dataSourceCustomerFiltered = new MatTableDataSource();
  filterType;
  tableFilters = [];

  constructor(private dialog: MatDialog, 
    private customerService: CustomerService, 
    private spinnerService: NgxSpinnerService) { }

  ngOnInit(): void {
    this.loadCustomerData('active');
  }

  // Custromer changes start
  openCustomerUpload() {
    this.dialog.open(CustomerUploadComponent, {
      data: { lstCustomer: this.ELEMENT_DATA_CUSTOMER }
    }).afterClosed().subscribe(res => {
      this.loadCustomerData('active');
    });
  }

  refreshCustomerData() {
    this.resetFilters();
    this.loadCustomerData('active');
  }

  loadFilteredCustomerData(status) {
    this.customerService.getCustomerDataByStatus(status).subscribe((res: any) => {
      this.ELEMENT_DATA_CUSTOMER = res.result;
      this.dataSourceCustomerFiltered.data = this.ELEMENT_DATA_CUSTOMER;
      this.dataSourceCustomerFiltered.paginator = this.paginator;
      this.dataSourceCustomerFiltered.sort = this.sort;
    });
  }

  loadCustomerData(status) {
    this.spinnerService.show();
    this.customerService.getCustomerDataByStatus(status).subscribe((res: any) => {
      this.ELEMENT_DATA_CUSTOMER = res.result;

      this.dataSourceCustomer.data = this.ELEMENT_DATA_CUSTOMER;
      this.dataSourceCustomer.paginator = this.paginator;
      this.dataSourceCustomer.sort = this.sort;

      this.dataSourceCustomerFiltered.data = this.ELEMENT_DATA_CUSTOMER;
      this.dataSourceCustomerFiltered.paginator = this.paginator;
      this.dataSourceCustomerFiltered.sort = this.sort;

      this.dataSourceCustomerFiltered.filterPredicate =
        (data, filtersJson: string) => {
          const matchFilter = [];
          const filters = JSON.parse(filtersJson);
          filters.forEach(filter => {
            if (filter.id == 'status') {
              let val = data["isActive"] === null ? '' : data["isActive"];
              val = val ? "active" : "inactive"; 
              if (val === filter.value.toLowerCase().trim()) {
                matchFilter.push(true);
              } else {
                matchFilter.push(false);
              }
            } else {
              const val = data[filter.id] === null ? '' : data[filter.id].toString().toLowerCase().trim();
              matchFilter.push(val.toLowerCase().includes(filter.value.toLowerCase()));
            }
            
          });
          return matchFilter.every(Boolean);
        };

      this.dataSourceCustomer.filterPredicate =
        (data, filtersJson: string) => {
          const matchFilter = [];
          const filters = JSON.parse(filtersJson);
          filters.forEach(filter => {
            if (filter.id == 'status') {
              let val = data["isActive"] === null ? '' : data["isActive"];
              val = val ? "active" : "inactive"; 
              if (val === filter.value.toLowerCase().trim()) {
                matchFilter.push(true);
              } else {
                matchFilter.push(false);
              }
            } else {
              const val = data[filter.id] === null ? '' : data[filter.id].toString().toLowerCase().trim();
              matchFilter.push(val.toLowerCase().includes(filter.value.toLowerCase()));
            }
          });
          return matchFilter.every(Boolean);
        };        
    });   
    this.spinnerService.hide(); 
  }


  // Custromer changes end

  openAddUpdateDialog(elem) {
    this.dialog.open(CustomerAddUpdateComponent, {
      width: '70%',
      data: { customer: elem, lstCustomers: this.ELEMENT_DATA_CUSTOMER }
    }).afterClosed().subscribe(res => {
      this.loadCustomerData('active');
    });
  }

  openConfirmDeleteDialog(element) {
    this.dialog.open(CustomerDeleteComponent, {
      width: '51%',
      data: element
    }).afterClosed().subscribe(res => {
      this.loadCustomerData('active');
    });
  }

  applyFilter(filterValue: string, filter) {
    switch (filter.toLowerCase()) {
      case "customername":
        this.filterType = "firstName"
        break;
      case "mobilenumber":
        this.filterType = "mobileNumber"
        break;
      case "category":
        this.filterType = "category"
        break;
      case "isactive":
        this.filterType = "status"
        this.loadFilteredCustomerData(filterValue.trim().toLowerCase());
        break;
      default:
        this.filterType = ""
    }

    if (this.filterType != "") {
      let filterIndex = this.tableFilters.findIndex(x => x.id === this.filterType);
      if (filterIndex > -1) {
        this.tableFilters[filterIndex].value = filterValue.trim().toLowerCase()
      } else {
        this.tableFilters.push({
          id: this.filterType,
          value: filterValue.trim().toLowerCase()
        });
      }
      this.dataSourceCustomerFiltered.filter = JSON.stringify(this.tableFilters);
      if (this.dataSourceCustomerFiltered.paginator) {
        this.dataSourceCustomerFiltered.paginator.firstPage();
      }
    }
    else {
      this.dataSourceCustomerFiltered.data = this.dataSourceCustomer.data;
      this.dataSourceCustomerFiltered.paginator = this.dataSourceCustomer.paginator;
    }
  }

  resetFilters() {
    this.tableFilters = [];
    this.filterType = "";
    this.customerName = "";
    this.mobileNumber = "";
    this.category = "";
    this.isActive = "";
    this.dataSourceCustomer.filter = "";
    this.dataSourceCustomerFiltered.filter = "";
  }
}