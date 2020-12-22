import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { CustomerService } from '../shared/customer.service';
import { FormBuilder, FormGroup, Validators, FormControl, AbstractControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ChangePassword } from '../models/change-password';
import { Router } from '@angular/router';
import { PasswordValidator } from '../shared/password.validator';
import { SharedService } from '../shared/shared.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  frmChangePassword: FormGroup;

  changePasswordModel: ChangePassword = new ChangePassword();

  constructor(private spinnerService: NgxSpinnerService,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private sharedService: SharedService) { }

  ngOnInit(): void {
    let user = JSON.parse(sessionStorage.getItem('user'));

    this.changePasswordModel.id = user.id;
    this.changePasswordModel.oldPassword = "";
    this.changePasswordModel.newPassword = "";
    this.changePasswordModel.confirmPassword = "";


    this.createForm();
  }

  createForm() {
    this.frmChangePassword = this.fb.group({
      id: [this.changePasswordModel.id],
      oldPassword: [this.changePasswordModel.oldPassword, [Validators.required, Validators.maxLength(50)]],
      newPassword: [this.changePasswordModel.newPassword, [Validators.required, Validators.maxLength(50)]],
      confirmPassword: [this.changePasswordModel.confirmPassword, [Validators.required, Validators.maxLength(50)]]
    }, {
      validators: [
        PasswordValidator.mustNotMatch('oldPassword', 'newPassword'),
        PasswordValidator.mustMatch('newPassword', 'confirmPassword')]
    });
  }

  getErrorMessage(control: string) {
    switch (control) {
      case 'oldPassword':
        return this.frmChangePassword.get('oldPassword').hasError('required') ? 'Old Password Is Required' :
          this.frmChangePassword.get('oldPassword').hasError('maxlength') ? 'Old Password Can Be Max 50 Characters Long' : '';
      case 'newPassword':
        return this.frmChangePassword.get('newPassword').hasError('required') ? 'New Password Is Required' :
          this.frmChangePassword.get('newPassword').hasError('maxlength') ? 'New Password Can Be Max 50 Characters Long' :
          this.frmChangePassword.get('newPassword').hasError('mustNotMatch') ? 'Old Password And New Password Must Not Match' : '';
      case 'confirmPassword':
        return this.frmChangePassword.get('confirmPassword').hasError('required') ? 'Confirm Password Is Required' :
          this.frmChangePassword.get('confirmPassword').hasError('maxlength') ? 'Confirm Password Can Be Max 50 Characters Long' :
            this.frmChangePassword.get('confirmPassword').hasError('mustMatch') ? 'New Password And Confirm Password Must Match' : '';
    }
  }


  changePassword(frmChangePassword) {
    this.spinnerService.show();
    let changePassword: ChangePassword = this.getChangePasswordModel(frmChangePassword);
    this.customerService.changePassword(changePassword).subscribe((event: any) => {
      this.spinnerService.hide();
      if (event.status == 200) {
        if (event.statusText == 'Password Changed Successfully!!') {
          this.toastr.success(event.statusText);
          this.navigateTo();
        }
        else {
          this.toastr.error(event.statusText);
        }
      }
      else {
        this.toastr.error('Server error. Please try after sometime.');
      }
    },
      error => {
        this.spinnerService.hide();
        this.toastr.error(error.error.message);
      });
  }

  getChangePasswordModel(frmChangePassword): ChangePassword {
    let changePassword: ChangePassword = new ChangePassword();
    changePassword.id = frmChangePassword.id;
    changePassword.oldPassword = frmChangePassword.oldPassword;
    changePassword.newPassword = frmChangePassword.newPassword;
    changePassword.confirmPassword = frmChangePassword.confirmPassword;
    return changePassword;
  }

  navigateTo() {
    this.sharedService.navigateToUsetLandingPageByRole();
  }
}