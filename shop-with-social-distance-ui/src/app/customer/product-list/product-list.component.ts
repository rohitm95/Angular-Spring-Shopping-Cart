import { Component, OnInit, OnChanges, SimpleChanges, ViewChild, ElementRef } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { CheckoutComponent } from '../checkout/checkout.component';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/shared/cart.service';
import { FormBuilder, Validators, FormGroup, FormControl } from '@angular/forms';
import { InventoryService } from 'src/app/shared/inventory.service';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { filter } from 'rxjs/operators';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})

export class ProductListComponent implements OnInit {

  productDetails: any;
  term: string;
  todayDate: Date = new Date();
  inventoryProductList = [];
  productCategoryList = [];
  productCategoryTempList = [];
  inventoryProductCount = 0;
  cartProductList = [];
  cartProductListCount = 0;
  productList;
  pageSize = 5;
  grandTotal = 0;
  checkbox: Boolean;
  checked: Boolean;
  selectvalue: 10;
  totalsize: number = 0;
  searchItem: string = '';
  searchCheck:string='';
  listForm: FormGroup = new FormGroup({
    qty: new FormControl('')
  });
  filterCategories: Array<string> = [];
  @ViewChild('listItems', { static: false }) listItems: ElementRef;
  @ViewChild('QuantityDropDown', { static: false }) qualityDropDown: ElementRef;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['item'];
  dataSource = new MatTableDataSource();
  dataSourceFilter = new MatTableDataSource();
  ELEMENT_DATA = [];
  filterType: string = '';
  tableFilters = [];
  filterText: string = '';


  constructor(public cartservice: CartService,
    private inventoryService: InventoryService,
    private toastr: ToastrService,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private spinnerService: NgxSpinnerService) {
    this.listForm = this.formBuilder.group({
      qty: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.getCategories();
    this.getInventoryList(0, this.pageSize);
  }

  getCategories() {
    this.spinnerService.show();
    this.inventoryService.getCategories().subscribe((res: any) => {
      this.productCategoryList = res.result;
      this.spinnerService.hide();
    },
      error => {
        this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  getInventoryList(pageNo: number, size: number) {
    this.listForm.reset();
    this.spinnerService.show();
    if(this.searchItem.length!=0){
      this.searchFilter();
      return;
    }
    if (pageNo * size >= this.totalsize) {
      size = this.totalsize - (pageNo - 1) * size;
    }
    this.inventoryService.getInventoryItemsForCustomer(this.filterCategories.toLocaleString(), pageNo, size)
      .subscribe((res: any) => {
        this.totalsize = res.totalElements; 
        if (this.inventoryProductList.length == 0) {
          this.inventoryProductList = res.result;
        } else {
          if (JSON.stringify(this.inventoryProductList) != JSON.stringify(res.result)) {

            res.result.forEach(element => {
              const result = this.inventoryProductList.filter(f => f.id == element.id);
              if (result.length == 0) {
                this.inventoryProductList.push(element);
      
              }
            });
          }
        }
        if (this.searchItem === '') {
          this.inventoryProductCount = this.totalsize;
        }

        this.ELEMENT_DATA = this.inventoryProductList;
        this.dataSource.data = this.ELEMENT_DATA;
        //this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        this.dataSourceFilter.data = this.ELEMENT_DATA;
        //this.dataSourceFilter.paginator = this.paginator;
        this.dataSourceFilter.sort = this.sort;
        this.dataSourceFilter.filterPredicate =
          (data, filtersJson: string) => {
            const matchFilter = [];
            const filters = JSON.parse(filtersJson);
            filters.forEach(filter => {
              const val = data[filter.id] == null ? '' : data[filter.id].toString();
              if (val != undefined && val.toLowerCase().trim().includes(filter.value.toLowerCase())) {
                matchFilter.push(true);
              }
              else {
                matchFilter.push(false);
              }
            });
            return matchFilter.every(Boolean);
          };

        this.spinnerService.hide();
        this.checked = false;
        //this.updateCount();
      },
        error => {
          this.toastr.error('Connection Error!');
          this.spinnerService.hide();
        });
  }

  validateCartEntry(product: Product, quantity) {
    if (quantity.value === '0' || quantity === null) {
      this.toastr.error('Quantity cannot be zero');
      return false;
    } else {
      this.addProductToCart(product, quantity.value);
      quantity.value = '0';
    }
  }

  addProductToCart(product, qty) {
    if (this.cartProductList.length === 0) {
      // product.qty = qty;
      product.noOfItemsOrderded = qty;
      this.grandTotal = this.grandTotal + (product.price * product.noOfItemsOrderded);
      this.cartProductList.push({ product });
      this.toastr.success('The product is added in the cart');
    }
    else {
      for (let i = 0; i < this.cartProductList.length; i++) {
        if (this.cartProductList[i].product.itemName === product.itemName) {
          if (this.cartProductList[i].product.noOfItemsOrderded === qty) {
            this.toastr.error('The product is added in the cart already with same quantity');
            break;
          }
          else if (this.cartProductList[i].product.noOfItemsOrderded < qty) {
            this.grandTotal = this.grandTotal + (product.price * (qty - product.noOfItemsOrderded));
            // this.cartProductList[i].product.qty = qty;
            this.cartProductList[i].product.noOfItemsOrderded = qty;
            this.toastr.success('Quantity Updated!');
            break;
          }
          else if (this.cartProductList[i].product.noOfItemsOrderded > qty) {
            this.grandTotal = this.grandTotal - (product.price * (product.noOfItemsOrderded - qty));
            // this.cartProductList[i].product.qty = qty;
            this.cartProductList[i].product.noOfItemsOrderded = qty;
            this.toastr.success('Quantity Updated!');
            break;
          }
        }
        else {
          if (i === (this.cartProductList.length - 1)) {
            // product.qty = qty;
            product.noOfItemsOrderded = qty;
            this.grandTotal = this.grandTotal + (product.price * product.noOfItemsOrderded);
            this.cartProductList.push({ product });
            this.toastr.success('The product is added in the cart');
            break;
          }
        }
      }
    }

    this.cartProductListCount = this.cartProductList.length;
  }

  openTimeslots() {
    if (this.cartProductListCount !== 0) {
      const dialogRef = this.dialog.open(CheckoutComponent,
        { data: { cartProductList: this.cartProductList, totalAmount: this.grandTotal } })
        .afterClosed().subscribe(res => {
          //console.log('Dialog closed----->' + res);
          if (res === 'Done') {
            this.cartProductList = [];
            this.cartProductListCount = 0;
            this.grandTotal = 0;
            this.getInventoryList(0, this.pageSize);
          }
        });
    } else {
      this.toastr.error('You can\'t checkout with empty cart!');
    }
  }

  deleteItem(i, cartProduct) {
    let amount = cartProduct.noOfItemsOrderded * cartProduct.price;
    this.grandTotal = this.grandTotal - amount;
    this.cartProductList.splice(i, 1);
    this.cartProductListCount = this.cartProductList.length;
    this.toastr.success('The item is removed from Cart!');

  }

  filterByCategory(event: any) {

    if (event.checked) {
      this.filterCategories.push(event.source.value);
    } else {
      this.filterCategories.splice(this.filterCategories.findIndex(x => x === event.source.value), 1);
    }
    this.paginator.pageIndex = 0;
    this.inventoryProductList.length = 0;
    if (this.searchItem != '') {
      this.searchFilter();
      return;
    }
    if (!event.checked && this.searchItem != '') {
      this.searchFilter();
    }
    else
      this.getInventoryList(0, this.pageSize);

  }

  textChange(event) {
    this.term = event;
    this.updateCount();
  }

  updateCount() {
    setTimeout(() => this.inventoryProductCount = (this.listItems.nativeElement as HTMLDivElement).childElementCount);
  }

  searchFilter() {
    this.searchCheck=this.searchItem;
    this.listForm.reset();
    this.spinnerService.show();
    this.inventoryProductList.length = 0;
    this.inventoryService.getSearchedItemsForCustomer(this.filterCategories.toLocaleString(), this.searchItem)
      .subscribe((res: any) => {
        this.totalsize = res.totalElements;
        this.inventoryProductList = res.result;


        this.inventoryProductCount = this.totalsize;
        this.ELEMENT_DATA = this.inventoryProductList;
        this.dataSource.data = this.ELEMENT_DATA;
        this.dataSource.sort = this.sort;
        this.dataSourceFilter.data = this.ELEMENT_DATA;
        this.dataSourceFilter.sort = this.sort;
        this.spinnerService.hide();
      },
        error => {
          this.toastr.error('data not found');
          this.inventoryProductList.length = 0;
          this.dataSourceFilter.data = [];
          this.dataSource.data = [];
          this.inventoryProductCount = 0;
          this.spinnerService.hide();
        });
  }
  clearSearchFilter(){
      this.searchCheck="";
      this.searchItem="";
      this.inventoryProductList.length = 0;
      this.getInventoryList(0, this.pageSize);
      this.paginator.pageIndex = 0;
  }
  resetFilters() {
    
    if (this.filterCategories.length !== 0) {
            this.checked = true;
      if (this.checkbox == true)
        this.checkbox = false;
      this.filterCategories = []
      this.filterCategories.length = 0;
      this.inventoryProductList.length = 0;
      this.paginator.pageIndex = 0;
      if (this.searchItem !== '') {
        this.searchFilter();
      }
      else
        this.getInventoryList(0, this.pageSize);


    }
  }

  onOptionsSelectedup(event) {
    this.inventoryProductList.length = 0;
    this.pageSize = event.pageSize;
    let pageCount = this.totalsize / this.pageSize;
    if (event.previousPageIndex < event.pageIndex && event.pageIndex != pageCount - 1) {
      this.getInventoryList(event.pageIndex , this.pageSize);
    }
    else {
      this.getInventoryList(event.pageIndex, this.pageSize);
    }

  }
}