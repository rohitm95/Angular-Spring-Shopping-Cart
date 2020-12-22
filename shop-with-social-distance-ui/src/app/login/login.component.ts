import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';
import { UserService } from '../shared/user.service';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isClicked = false;
  form: FormGroup = new FormGroup({
    mobileNumber: new FormControl(''),
    password: new FormControl(''),
    // form variable
  });

  constructor(fb: FormBuilder,
    private toastr: ToastrService,
    private userService: UserService,
    private dialog: MatDialog) {
    this.form = fb.group({
      // validation of form data
      mobileNumber: [
        '',
        [Validators.required, Validators.pattern('^((\\+91-?)|0)?[0-9]{10}$')],
      ],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (sessionStorage.getItem('isLoggedIn') === 'true') {
      this.userService.navigateTo();
    }
  }

  get f() {
    return this.form.controls;
  }

  submit(form: FormGroup) {
    this.isClicked = true;
    this.userService.login(form.value);
  }

  forgot() {
    this.toastr.warning('Please contact the Admin to get a new password at admin@covidinventory.com');
  }

  toggleForgotPassword() {
    this.dialog.open(ForgotPasswordComponent, {
      width: '25%',
    }).afterClosed().subscribe(res => {
    });
  }
}
