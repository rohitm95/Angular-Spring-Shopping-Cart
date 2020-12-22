import { Component, OnInit, Inject } from '@angular/core';
import { CustomerService } from 'src/app/shared/customer.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { MatTableDataSource } from '@angular/material/table';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Customer, Store } from '../../../models/customer.model';
import { CustomerToAddUpdate } from '../../../models/customer.model';
import { StoreService } from 'src/app/shared/store.service';
import { PasswordValidator } from 'src/app/shared/password.validator';

@Component({
  selector: 'app-customer-addupdate',
  templateUrl: './customer-addupdate.component.html',
  styleUrls: ['./customer-addupdate.component.scss']
})
export class CustomerAddUpdateComponent implements OnInit {

  action: string = '';
  role: any;
  userDetail;
  isClicked = false;
  dataSource = new MatTableDataSource();

  frmCustomer: FormGroup;

  roles: any[];
  categories: any[];
  gender: any[];
  submitted = false;
  customer: Customer = new Customer();
  post: any = '';

  constructor(private spinnerService: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<CustomerAddUpdateComponent>,
    private storeService: StoreService) {

  }

  ngOnInit(): void {
    this.userDetail = JSON.parse(sessionStorage.getItem('user'));
    this.role = sessionStorage.getItem('userRole');
    this.initializeCustomerModel();
    this.roles = this.customerService.getCustomerRoles();
    this.categories = this.customerService.getCustomerCategories();
    this.gender = ["Male", "Female"];
    this.createForm();
  }

  createForm() {
    let emailPattern: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    let mobNumberPattern = "^((\\+91-?)|0)?[0-9]{10}$"
    let purchaseLimitPattern = /^[.\d]+$/

    this.frmCustomer = this.fb.group({
      id: [this.customer.id],
      username: [this.customer.username, [Validators.required, Validators.maxLength(50)]],
      firstName: [this.customer.firstName, [Validators.required, Validators.maxLength(50)]],
      lastName: [this.customer.lastName, [Validators.maxLength(50)]],
      emailId: [this.customer.emailId, [Validators.required, Validators.email, Validators.pattern(emailPattern), Validators.maxLength(100)]],
      mobileNumber: [this.customer.mobileNumber, [Validators.required, Validators.pattern(mobNumberPattern)]],
      role: [this.customer.role, Validators.required],
      isActive: [this.customer.isActive, Validators.required],
      category: [this.customer.category, Validators.required],
      afd_purchase_limit: [this.customer.afd_purchase_limit, [Validators.required, Validators.pattern(purchaseLimitPattern)]],
      nonAFD_purchase_limit: [this.customer.nonAFD_purchase_limit, [Validators.required, Validators.pattern(purchaseLimitPattern)]],
      gender: [this.customer.gender, Validators.required]
       },
       {
      validators: [PasswordValidator.mustBeLeser('nonAFD_purchase_limit', 'afd_purchase_limit')]
       });

  }

  initializeCustomerModel() {
    this.customer.id = null;
    this.customer.username = null;
    this.customer.firstName = null;
    this.customer.lastName = null;
    this.customer.emailId = null;
    this.customer.mobileNumber = null;
    this.customer.role = null;
    this.customer.isActive = null;
    this.customer.category = null;
    this.customer.afd_purchase_limit = null;
    this.customer.nonAFD_purchase_limit = null;
    this.customer.gender = null;
    if (this.data.customer == null) {
      this.action = 'Add';
    }
    else {
      this.action = 'Update';
      this.customer.id = this.data.customer.id;
      this.customer.username = this.data.customer.username;
      this.customer.firstName = this.data.customer.firstName;
      this.customer.lastName = this.data.customer.lastName;
      this.customer.emailId = this.data.customer.emailId;
      this.customer.mobileNumber = this.data.customer.mobileNumber;
      this.customer.role = this.data.customer.role.id;
      this.customer.isActive = this.data.customer.isActive;
      this.customer.category = this.data.customer.category;
      this.customer.afd_purchase_limit = this.data.customer.afd_purchase_limit;
      this.customer.nonAFD_purchase_limit = this.data.customer.nonAFD_purchase_limit;
      this.customer.gender = this.data.customer.gender;
    }
  }

  getErrorMessage(control: string) {
    switch (control) {
      case 'username':
        return this.frmCustomer.get('username').hasError('required') ? 'User Name Is Required' :
          this.frmCustomer.get('username').hasError('maxlength') ? 'User Name can be max 50 characters long' : '';
      case 'gender':
        return this.frmCustomer.get('gender').hasError('required') ? 'Gender Is Required' : '';
      case 'firstName':
        return this.frmCustomer.get('firstName').hasError('required') ? 'First Name Is Required' :
          this.frmCustomer.get('username').hasError('maxlength') ? 'First Name can be max 50 characters long' : '';
      case 'lastName':
        return this.frmCustomer.get('lastName').hasError('required') ? 'Last Name Is Required' :
          this.frmCustomer.get('lastName').hasError('maxlength') ? 'Last Name can be max 50 characters long' : '';
      case 'emailId':
        return this.frmCustomer.get('emailId').hasError('required') ? 'Email Id Is Required' :
          this.frmCustomer.get('emailId').hasError('pattern') ? 'Invalid Email Id' :
            this.frmCustomer.get('emailId').hasError('maxlength') ? 'Email Id can be max 100 characters long' : '';
      case 'mobileNumber':
        return this.frmCustomer.get('mobileNumber').hasError('required') ? 'Mobile Number Is Required' :
          this.frmCustomer.get('mobileNumber').hasError('pattern') ? 'Invalid Mobile Number' : '';
      case 'category':
        return this.frmCustomer.get('category').hasError('required') ? 'Category Is Required' : '';
      case 'role':
        return this.frmCustomer.get('role').hasError('required') ? 'Role Is Required' : '';
      case 'afd_purchase_limit':
        return this.frmCustomer.get('afd_purchase_limit').hasError('required') ? 'Purchase Limt Per Year Is Required' :
          this.frmCustomer.get('afd_purchase_limit').hasError('pattern') ? 'Invalid Purchase Limt Per Year' :
            this.frmCustomer.get('afd_purchase_limit').hasError('mustBeLeser') ? 'Purchase Limt Per Month Must Be Leser Than Purchase Limt Per Year' : '';
      case 'nonAFD_purchase_limit':
        return this.frmCustomer.get('nonAFD_purchase_limit').hasError('required') ? 'Purchase Limt Per Month Is Required' :
          this.frmCustomer.get('nonAFD_purchase_limit').hasError('pattern') ? 'Invalid Purchase Limt Per Month' :
            this.frmCustomer.get('nonAFD_purchase_limit').hasError('mustBeLeser') ? 'Purchase Limt Per Month Must Be Leser Than Purchase Limt Per Year' : '';
      case 'isActive':
        return this.frmCustomer.get('isActive').hasError('required') ? 'Status Is Required' : '';
    }
  }

  getCustomerModel(frmCustomer): CustomerToAddUpdate {
    let customerToAddUpdate: CustomerToAddUpdate = new CustomerToAddUpdate();
    customerToAddUpdate.id = frmCustomer.id;
    customerToAddUpdate.username = frmCustomer.username;
    customerToAddUpdate.firstName = frmCustomer.firstName;
    customerToAddUpdate.lastName = frmCustomer.lastName;
    customerToAddUpdate.emailId = frmCustomer.emailId;
    customerToAddUpdate.mobileNumber = frmCustomer.mobileNumber;
    customerToAddUpdate.role.id = frmCustomer.role;
    customerToAddUpdate.isActive = frmCustomer.isActive;
    customerToAddUpdate.category = frmCustomer.category;
    customerToAddUpdate.afd_purchase_limit = parseFloat(frmCustomer.afd_purchase_limit);
    customerToAddUpdate.nonAFD_purchase_limit = parseFloat(frmCustomer.nonAFD_purchase_limit);
    customerToAddUpdate.dateOfJoin = "";
    customerToAddUpdate.gender = frmCustomer.gender;
    return customerToAddUpdate;
  }

  isCostomerAlreadyExists(frmCustomer): boolean {
    let isExists = true;
    var userName = this.data.lstCustomers.filter(function (c) {
      return c.username.toLowerCase() == frmCustomer.username.toLowerCase() && c.id != frmCustomer.id
    });
    if (userName != '') {
      this.toastr.error('User Name Already Used');
      return isExists;
    }
    var emailId = this.data.lstCustomers.filter(function (c) {
      return c.emailId.toLowerCase() == frmCustomer.emailId.toLowerCase() && c.id != frmCustomer.id
    });
    if (emailId != '') {
      this.toastr.error('Email Id Already Used');
      return isExists;
    }
    var mobileNumber = this.data.lstCustomers.filter(function (c) {
      return c.mobileNumber == frmCustomer.mobileNumber && c.id != frmCustomer.id
    });
    if (mobileNumber != '') {
      this.toastr.error('Mobile Number Already Used');
      return isExists;
    }
    isExists = false;
    return isExists;
  }

  addUpdateCustomer(frmCustomer) {
    if (!this.isCostomerAlreadyExists(frmCustomer)) {
      let customerToAddUpdate = this.getCustomerModel(frmCustomer);
      this.spinnerService.show();
      if (customerToAddUpdate.id == null) // Add customer
      {
        this.customerService.addCustomer(customerToAddUpdate).subscribe((event: any) => {
          this.spinnerService.hide();
          this.toastr.success('Customer Updated Successfully!');
          this.dialogRef.close();
        },
          error => {
            this.spinnerService.hide();
            this.toastr.error(error.error.message);
            this.dialogRef.close();
          });
      }
      else if (customerToAddUpdate.id != null) // Update customer
      {
        this.customerService.updateCustomer(customerToAddUpdate, customerToAddUpdate.id).subscribe((event: any) => {
          this.spinnerService.show();
          this.toastr.success('Customer Updated Successfully!');
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
}