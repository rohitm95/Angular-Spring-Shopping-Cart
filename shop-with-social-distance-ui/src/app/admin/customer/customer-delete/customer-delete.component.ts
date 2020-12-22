import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';

import { CustomerService } from 'src/app/shared/customer.service';

@Component({
  selector: 'app-customer-delete',
  templateUrl: './customer-delete.component.html',
  styleUrls: ['./customer-delete.component.scss']
})
export class CustomerDeleteComponent implements OnInit {

  isClicked = false;

  constructor(private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private customerService: CustomerService,
    private dialogRef: MatDialogRef<CustomerDeleteComponent>,
    private spinnerService: NgxSpinnerService) {

  }

  ngOnInit(): void {
  }

  deleteCustomer(customerId) {
    this.spinnerService.show();
    this.customerService.deleteCustomerData(customerId).subscribe(res => {
      this.toastr.success('Customer Details Deleted Successfully!');
      this.dialogRef.close();
      this.spinnerService.hide();
    },
      error => {
        console.log(error);
        if (error.status === 200) {
          this.toastr.success('Customer Details Deleted Successfully!');
          this.dialogRef.close();
        } else {
          this.toastr.error('An Error Ocurred, Please Try again!');
          this.dialogRef.close();
        }
        this.spinnerService.hide();
      });
  }
}
