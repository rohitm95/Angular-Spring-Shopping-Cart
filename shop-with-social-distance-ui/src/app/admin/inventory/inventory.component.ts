import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { InventoryUploadComponent } from './inventory-upload/inventory-upload.component';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { InventoryService } from 'src/app/shared/inventory.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { MatSort } from '@angular/material/sort';
import { InventoryAddupdateComponent } from './inventory-addupdate/inventory-addupdate.component';
import { InventoryDeleteComponent } from './inventory-delete/inventory-delete.component';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss']
})
export class InventoryComponent implements OnInit {

  ELEMENT_DATA = [];
  displayedColumns: string[] = ['item_no', 'group', 'itemName', 'price', 'stock', 'category', 'actions'];
  dataSource = new MatTableDataSource();
  dataSourceFiltered = new MatTableDataSource();
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  tableFilters = [];

  filterType;

  categories = [];
  groups = [];

  itemNumber: string = '';
  group: string = '';
  name: string = '';
  category: string = '';

  constructor(private toastr: ToastrService, private dialog: MatDialog,
    private inventoryService: InventoryService, private spinnerService: NgxSpinnerService) { }

  ngOnInit(): void {
    this.loadInventoryData();
    this.getGroups();
    this.getCategories();
  }

  openInventoryUpload() {
    this.dialog.open(InventoryUploadComponent).afterClosed().subscribe((res: any) => {
      this.loadInventoryData();
    });
  }

  loadInventoryData() {
    this.ELEMENT_DATA = [];
    this.spinnerService.show();
    this.inventoryService.getInventoryItems(null).subscribe((res: any) => {
      this.ELEMENT_DATA = res.result;

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
            const val = data[filter.id] === null ? '' : data[filter.id].toString().toLowerCase().trim();
            if (filter.id == 'group') {
              if (val === filter.value.toLowerCase().trim()) {
                matchFilter.push(true);
              } else {
                matchFilter.push(false);
              }
            } else {
              matchFilter.push(val.toLowerCase().includes(filter.value.toLowerCase()));
            }
          });
          return matchFilter.every(Boolean);
        };

      this.dataSource.filterPredicate =
        (data, filtersJson: string) => {
          const matchFilter = [];
          const filters = JSON.parse(filtersJson);
          filters.forEach(filter => {
            const val = data[filter.id] === null ? '' : data[filter.id].toString();
            if (filter.id == 'group') {
              if (val === filter.value.toLowerCase().trim()) {
                matchFilter.push(true);
              } else {
                matchFilter.push(false);
              }
            } else {
              matchFilter.push(val.toLowerCase().includes(filter.value.toLowerCase()));
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

  refreshInventoryData() {
    this.loadInventoryData();
    this.resetFilters();
  }

  applyFilter(filterValue: string, filter) {
    switch (filter.toLowerCase()) {
      case "itemnumber":
        this.filterType = "itemNumber"
        break;
      case "group":
        this.filterType = "group"
        break;
      case "name":
        this.filterType = "itemName"
        break;
      case "category":
        this.filterType = "category"
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
    this.itemNumber = '';
    this.group = '';
    this.name = '';
    this.category = '';
    this.dataSourceFiltered.filter = "";
    this.tableFilters = [];
  }

  openAddUpdateDialog(elem) {
    this.dialog.open(InventoryAddupdateComponent, {
      width: '70%',
      data: { inventory: elem, lstInventories: this.ELEMENT_DATA }
    }).afterClosed().subscribe(res => {
      this.refreshInventoryData();
    });
  }

  openConfirmDeleteDialog(element) {
    this.dialog.open(InventoryDeleteComponent, {
      width: '51%',
      data: element
    }).afterClosed().subscribe(res => {
      this.refreshInventoryData();     
    });
  }

  getGroups(){
    this.inventoryService.getGroups().subscribe((res: any) => {
      this.groups = res.result;
    });
  }

  getCategories(){
    this.inventoryService.getCategories().subscribe((res: any) => {
      this.categories = res.result;
    });
  }
}
