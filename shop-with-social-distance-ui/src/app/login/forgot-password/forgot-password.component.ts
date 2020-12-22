import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { UserService } from 'src/app/shared/user.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { PasswordValidator } from 'src/app/shared/password.validator';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  forgotPasswordOption: boolean = true;
  forgotPasswordValue: string = '';
  placeHolder: string = 'Enter Mobile No.';
  lable: string = 'Mobile No.';

  emailPattern: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  mobNumberPattern = "^((\\+91-?)|0)?[0-9]{10}$"
  frmForgotPassword: FormGroup;
  isValidForm: boolean = false;
  isValidControl: boolean = false;


  constructor(private spinnerService: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<ForgotPasswordComponent>,
    private fb: FormBuilder,) { }

  ngOnInit(): void {
    this.generateForm();
  }

  generateForm() {
    this.frmForgotPassword = this.fb.group({
      forgotPasswordOption: [this.forgotPasswordOption],
      forgotPasswordValue: [this.forgotPasswordValue, [Validators.required]]
    }, {
      validators: [
        PasswordValidator.mustValidByOption('forgotPasswordOption', 'forgotPasswordValue')]
    });
  }

  getErrorMessage() {
    let message: String = '';
    if (this.forgotPasswordOption) {
      message = 'Mobile No.';
    }
    else {
      message = 'Email Id';
    }

    return this.frmForgotPassword.get('forgotPasswordValue').hasError('required') ? message + ' Is Required' :
      this.frmForgotPassword.get('forgotPasswordValue').hasError('mustValidByOption') ? 'Invalid ' + message : '';
  }

  forgotPassword(frm) {
    let type: string = frm.forgotPasswordOption ? 'mobile' : 'email';
    let value: string = frm.forgotPasswordValue;
    this.userService.forgotPassword(type, value).subscribe((event: any) => {
      if (event.status == 200) {
        this.toastr.success(event.statusText);
        this.dialogRef.close();
      }
      else {
        this.toastr.success(event.statusText);
      }
    },
      error => {
        this.toastr.error(error.error.statusText);
      });
  }

  toggleForgotPasswordOptionsValue() {
    this.forgotPasswordOption = !this.forgotPasswordOption;
    this.forgotPasswordValue = '';
     this.frmForgotPassword.get('forgotPasswordValue').setValue('');
    if (this.forgotPasswordOption) {
      this.placeHolder = 'Enter Mobile No.';
      this.lable = 'Mobile No.';
    }
    else {
      this.placeHolder = 'Enter Email Id';
      this.lable = 'Email Id';
    }
  }
}