import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { NewCustomerComponent } from '../customer/new-customer/new-customer.component';
import { NgxSpinnerService } from 'ngx-spinner';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  responseData = [];
  isLoggedIn: string;
  user: any;
  token;
  role: string = sessionStorage.getItem('userRole');

  constructor(
    private router: Router,
    private http: HttpClient,
    private toastr: ToastrService,
    public dialog: MatDialog, private spinnerService: NgxSpinnerService,
    private env: EnvService
  ) { }

  login(formData) {
    //console.log('from inventory to login ' + this.env.apiUrl);
    this.spinnerService.show();
    this.http
      .post<any>(this.env.apiUrl + '/authenticate', {
        username: formData.mobileNumber,
        password: formData.password,
      })
      .subscribe(
        res => {
          //console.log(res);
          if (res.user.newUser === true) {
            this.dialog.open(NewCustomerComponent);
          }
          //this.toastr.success('Login Successful!');
          this.isLoggedIn = 'true';
          this.user = res.user;
          this.token = res.token;
          // console.log(this.isLoggedIn);
          // console.log(this.user);
          window.sessionStorage.setItem('token', res.token);
          window.sessionStorage.setItem('userRole', res.user.role.name);
          window.sessionStorage.setItem('isLoggedIn', this.isLoggedIn);
          window.sessionStorage.setItem('userName', res.user.firstName);
          window.sessionStorage.setItem('user', JSON.stringify(res.user));
          // console.log(res);
          this.role = sessionStorage.getItem('userRole'); // then can route depending on customer or admin or aggregator
          this.spinnerService.hide();
          this.navigateTo();
        },
        error => {
          this.toastr.error('Invalid (username or password) or User is no longer active!');
          this.spinnerService.hide();
        }
      );
  }

  navigateTo(): void {
    if (this.role === 'Admin') {
      this.router.navigate(['/admin/dashboard']); //  navigate to admin component
    }
    if (this.role === 'Customer') {
      this.router.navigate(['/customer/product-list']); //  navigate to customer component
    }
    if (this.role === 'Aggregator') {
      this.router.navigate(['/aggregator/orders']); // navigate to Aggregator component
    }
  }

  logout() {
    window.sessionStorage.removeItem('token');
    window.sessionStorage.removeItem('userRole');
    window.sessionStorage.removeItem('isLoggedIn');
    window.sessionStorage.removeItem('userName');
    window.sessionStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  forgotPassword(type: string, value: string) {
    return this.http.put<any>(this.env.apiUrl + '/api/users/forgotpassword/' + type + '/' + value, null);
  }
}
