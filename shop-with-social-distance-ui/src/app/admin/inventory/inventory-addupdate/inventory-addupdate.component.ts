import { Component, OnInit, Inject } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InventoryService } from 'src/app/shared/inventory.service';
import { Inventory, InventoryToAddUpdate } from 'src/app/models/inventory.model';
import { PasswordValidator } from 'src/app/shared/password.validator';
import { RxwebValidators } from "@rxweb/reactive-form-validators"
@Component({
  selector: 'app-inventory-addupdate',
  templateUrl: './inventory-addupdate.component.html',
  styleUrls: ['./inventory-addupdate.component.scss']
})
export class InventoryAddupdateComponent implements OnInit {

  action: string = '';
  role: any;
  userDetail;
  isClicked = false;
  dataSource = new MatTableDataSource();

  frmInventory: FormGroup;

  groups: any[];
  categories: any[];
  itemTypes: any[];
  yes_no: any[];
  true_false: any[];
  submitted = false;
  extensions: string[] = ["png", "jpg", "jpeg"];
  inventory: Inventory = new Inventory();
  imageSrc: string;
  imageFile: File;
  post: any = '';

  constructor(private spinnerService: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private inventoryService: InventoryService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<InventoryAddupdateComponent>) {

  }

  ngOnInit(): void {
    this.userDetail = JSON.parse(sessionStorage.getItem('user'));
    this.role = sessionStorage.getItem('userRole');
    this.initializeModel();
    this.getGroups();
    this.getCategories();
    this.itemTypes = this.inventoryService.getItemTypes();
    this.yes_no = ["Yes", "No"];
    this.true_false = ["True", "False"];
    this.createForm();
  }

  initializeModel() {
    this.inventory.id = null;
    this.inventory.itemNumber = null;
    this.inventory.group = null;
    this.inventory.itemName = null;
    this.inventory.price = null;
    this.inventory.stock = null;
    this.inventory.category = null;
    this.inventory.lowStockIndicator = null;
    this.inventory.toBeSoldOneItemPerOrder = null;
    this.inventory.volumePerItem = null;
    this.inventory.monthlyQuotaPerUser = null;
    this.inventory.itemType = null;
    this.inventory.yearlyQuotaPerUser = null;
    if (this.data.inventory == null) {
      this.action = 'Add';
    }
    else {
      this.action = 'Update';
      this.inventory.id = this.data.inventory.id;
      this.inventory.itemNumber = this.data.inventory.itemNumber;
      this.inventory.group = this.data.inventory.group;
      this.inventory.itemName = this.data.inventory.itemName;
      this.inventory.price = this.data.inventory.price;
      this.inventory.stock = this.data.inventory.stock;
      this.inventory.category = this.data.inventory.category;
      this.inventory.lowStockIndicator = this.data.inventory.lowStockIndicator ? "True" : "False";
      this.inventory.toBeSoldOneItemPerOrder = this.data.inventory.toBeSoldOneItemPerOrder ? "True" : "False";
      this.inventory.volumePerItem = this.data.inventory.volumePerItem;
      this.inventory.monthlyQuotaPerUser = this.data.inventory.monthlyQuotaPerUser;
      this.inventory.itemType = this.data.inventory.itemType;
      this.inventory.yearlyQuotaPerUser = this.data.inventory.yearlyQuotaPerUser;
    }
  }

  createForm() {
    let numberPattern = /^[.\d]+$/
    let quantityPattern = /^[0-9]+$/

    this.frmInventory = this.fb.group({
      id: [this.inventory.id],
      itemNumber: [this.inventory.itemNumber, [Validators.required]],
      group: [this.inventory.group, [Validators.required]],
      itemName: [this.inventory.itemName, [Validators.required]],
      price: [this.inventory.price, [Validators.required, Validators.pattern(numberPattern)]],
      stock: [this.inventory.stock, [Validators.required, Validators.pattern(quantityPattern)]],
      category: [this.inventory.category, [Validators.required]],
      lowStockIndicator: [this.inventory.lowStockIndicator, [Validators.required]],
      toBeSoldOneItemPerOrder: [this.inventory.toBeSoldOneItemPerOrder, [Validators.required]],
      volumePerItem: [this.inventory.volumePerItem, [Validators.required, Validators.pattern(quantityPattern)]],
      monthlyQuotaPerUser: [this.inventory.monthlyQuotaPerUser, [Validators.required, Validators.pattern(quantityPattern)]],
      itemType: [this.inventory.itemType, [Validators.required]],
      yearlyQuotaPerUser: [this.inventory.yearlyQuotaPerUser, [Validators.required, Validators.pattern(quantityPattern)]],
      image: [this.imageFile, [Validators.required, RxwebValidators.fileSize({ maxSize: 10000 }), RxwebValidators.extension({ extensions: this.extensions })]]
    }, {
      validators: [PasswordValidator.mustBeLeser('monthlyQuotaPerUser', 'yearlyQuotaPerUser')]
    });
  }

  getErrorMessage(control: string) {
    switch (control) {
      case 'itemNumber':
        return this.frmInventory.get('itemNumber').hasError('required') ? 'Item Number Is Required' : '';
      case 'group':
        return this.frmInventory.get('group').hasError('required') ? 'Group Is Required' : '';
      case 'itemName':
        return this.frmInventory.get('itemName').hasError('required') ? 'Item Name Is Required' :
          this.frmInventory.get('itemName').hasError('maxlength') ? 'Name can be max 100 characters long' : '';
      case 'price':
        return this.frmInventory.get('price').hasError('required') ? 'Price Is Required' :
          this.frmInventory.get('price').hasError('pattern') ? 'Invalid Price' : '';
      case 'stock':
        return this.frmInventory.get('stock').hasError('required') ? 'Stock Is Required' :
          this.frmInventory.get('stock').hasError('pattern') ? 'Invalid Stock' : '';
      case 'category':
        return this.frmInventory.get('category').hasError('required') ? 'Category Is Required' : '';
      case 'lowStockIndicator':
        return this.frmInventory.get('lowStockIndicator').hasError('required') ? 'Low Stock Indicator Is Required' : '';
      case 'toBeSoldOneItemPerOrder':
        return this.frmInventory.get('toBeSoldOneItemPerOrder').hasError('required') ? 'To Be Sold One Item Per Order Is Required' : '';
      case 'volumePerItem':
        return this.frmInventory.get('volumePerItem').hasError('required') ? 'Volume Per Item Is Required' :
          this.frmInventory.get('volumePerItem').hasError('pattern') ? 'Invalid Volume Per Item' : '';
      case 'monthlyQuotaPerUser':
        return this.frmInventory.get('monthlyQuotaPerUser').hasError('required') ? 'Monthly Quota Per User Is Required' :
          this.frmInventory.get('monthlyQuotaPerUser').hasError('pattern') ? 'Invalid Monthly Quota Per User' :
            this.frmInventory.get('yearlyQuotaPerUser').hasError('mustBeLeser') ? 'Monthy Quota Must Be Leser Then Yearly Quota' : '';
      case 'itemType':
        return this.frmInventory.get('itemType').hasError('required') ? 'Item Type Is Required' : '';
      case 'yearlyQuotaPerUser':
        return this.frmInventory.get('yearlyQuotaPerUser').hasError('required') ? 'Yearly Quota Per User Is Required' :
          this.frmInventory.get('monthlyQuotaPerUser').hasError('pattern') ? 'Invalid Yearly Quota Per User' :
            this.frmInventory.get('yearlyQuotaPerUser').hasError('mustBeLeser') ? 'Monthy Quota Must Be Leser Then Yearly Quota' : '';
      case 'image':
        return this.frmInventory.get('image').hasError('required') ? 'Image Is Required' :
          this.frmInventory.get('image').hasError('fileSize') ? 'Image size must be less than 10kb' :
            this.frmInventory.get('image').hasError('extension') ? 'File extension not allowed' : '';
    }
  }

  addUpdateInventory(frmInventory) {
    if (!this.isAlreadyExists(frmInventory)) {
      this.spinnerService.show();
      let inventoryToAddUpdate: InventoryToAddUpdate = this.getCustomerModel(frmInventory);
      const formData = new FormData();
      formData.append('file', this.imageFile);
      formData.append('inventoryData', new Blob([JSON.stringify(inventoryToAddUpdate)], {
        type: "application/json"
      }));
      if (frmInventory.id == null) // Add customer
      {
        this.inventoryService.addInventory(formData).subscribe((event: any) => {
          this.spinnerService.hide();
          this.toastr.success('Inventory Added Successfully!');
          this.dialogRef.close();
        },
          error => {
            this.spinnerService.hide();
            this.toastr.error(error.error.message);
            this.dialogRef.close();
          });
      }
      else if (frmInventory.id != null) // Update customer
      {
        this.inventoryService.updateInventory(formData, inventoryToAddUpdate.id).subscribe((event: any) => {
          this.spinnerService.hide();
          this.toastr.success('Inventory Updated Successfully!');
          this.dialogRef.close();
        },
          error => {
            this.spinnerService.hide();
            this.toastr.error(error.error.message);
            this.dialogRef.close();
          });
      }
    }
  }
  onImageChange(event: any) {

    event.preventDefault();
    this.imageFile = <File>event.target.files[0];
    console.log(this.imageFile)
    const reader = new FileReader();

    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      reader.readAsDataURL(file);

      reader.onload = () => {
        this.imageSrc = reader.result as string;
      }
    }
  }
  getCustomerModel(frmInventory): InventoryToAddUpdate {
    let inventoryToAddUpdate: InventoryToAddUpdate = new InventoryToAddUpdate();
    inventoryToAddUpdate.id = frmInventory.id;
    inventoryToAddUpdate.itemNumber = frmInventory.itemNumber;
    inventoryToAddUpdate.group = frmInventory.group;
    inventoryToAddUpdate.itemName = frmInventory.itemName;
    inventoryToAddUpdate.price = frmInventory.price;
    inventoryToAddUpdate.stock = frmInventory.stock;
    inventoryToAddUpdate.category = frmInventory.category;
    inventoryToAddUpdate.lowStockIndicator = frmInventory.lowStockIndicator;
    inventoryToAddUpdate.toBeSoldOneItemPerOrder = frmInventory.toBeSoldOneItemPerOrder;
    inventoryToAddUpdate.volumePerItem = frmInventory.volumePerItem;
    inventoryToAddUpdate.monthlyQuotaPerUser = frmInventory.monthlyQuotaPerUser;
    inventoryToAddUpdate.itemType = frmInventory.itemType;
    inventoryToAddUpdate.yearlyQuotaPerUser = frmInventory.yearlyQuotaPerUser;
    inventoryToAddUpdate.noOfItemsOrderded = frmInventory.noOfItemsOrderded;
    inventoryToAddUpdate.store.id = 1;
    return inventoryToAddUpdate;
  }

  getGroups() {
    this.inventoryService.getGroups().subscribe((res: any) => {
      this.groups = res.result;
    });
  }

  getCategories() {
    this.inventoryService.getCategories().subscribe((res: any) => {
      this.categories = res.result;
    });
  }

  isAlreadyExists(frmCustomer): boolean {
    let isExists = true;
    var itemNumber = this.data.lstInventories.filter(function (c) {
      return c.itemNumber.toLowerCase() == frmCustomer.itemNumber.toLowerCase() && c.id != frmCustomer.id
    });
    if (itemNumber != '') {
      this.toastr.error('Item Number Already Used');
      return isExists;
    }
    var itemName = this.data.lstInventories.filter(function (c) {
      return c.itemName.toLowerCase() == frmCustomer.itemName.toLowerCase() && c.id != frmCustomer.id
    });
    if (itemName != '') {
      this.toastr.error('Item Name Already Used');
      return isExists;
    }
    /*var group = this.data.lstInventories.filter(function (c) {
      return c.group == frmCustomer.group && c.id != frmCustomer.id 
    });
    if (group != '') {
      debugger;
      this.toastr.error('Group Already Used');
      return isExists;
    }
    var category = this.data.lstInventories.filter(function (c) {
      return c.category == frmCustomer.category && c.id != frmCustomer.id
    });
    if (category != '') {
      this.toastr.error('Category Already Used');
      return isExists;
    }*/
    isExists = false;
    return isExists;
  }
}