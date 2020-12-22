import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EnvService } from './env.service';
import { Customer, CustomerToAddUpdate } from '../models/customer.model';
import { ChangePassword } from '../models/change-password';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient, private env: EnvService) { }

  userToken = sessionStorage.getItem('token');
  headers = new HttpHeaders()
    .set('Authorization', 'Bearer ' + this.userToken)
    .set('Access-Control-Allow-Origin', '*');

  uploadCustomerData(custFormData) {
    return this.http.post<any>(this.env.apiUrl + '/api/user/upload', custFormData, {
      headers: this.headers, reportProgress: true,
      observe: 'events',
    });
  }

  // Customer changes start
  getCustomerData() {
    return this.http.get(this.env.apiUrl + '/api/user/customers', { headers: this.headers });
  }

  deleteCustomerData(customerId) {
    return this.http.delete(this.env.apiUrl + '/api/users/' + customerId, { headers: this.headers });
  }

  getCustomerRoles() {
    const roles = [{ id: 1, name: "Admin" }, { id: 2, name: "Customer" }, { id: 3, name: "Aggregator" }];
    return roles;
  }

  getCustomerCategories() {
    const categories = ["A", "B", "C"];
    return categories;
  }

  updateCustomer(customer: CustomerToAddUpdate, id: number) {
    return this.http.put<any>(this.env.apiUrl + '/api/users/' + id, customer, { headers: this.headers });
  }

  addCustomer(customer: CustomerToAddUpdate) {
    return this.http.post<any>(this.env.apiUrl + '/api/register', customer, { headers: this.headers });
  }
  // Customer changes end

  getCustomerDataByStatus(status: string) {
    let isActive: string = '';
    if (status == 'active') {
      isActive = 'true';
    }
    else if (status == 'inactive') {
      isActive = 'false';
    }
    return this.http.get(this.env.apiUrl + '/api/user/customers?isActive=' + isActive, { headers: this.headers });
  }

  changePassword(changePassword: ChangePassword) {
    let json =
    {
      "id": changePassword.id,
      "password": changePassword.newPassword,
      "oldPassword": changePassword.oldPassword
    };
    return this.http.put<any>(this.env.apiUrl + '/api/users/password/change/' + json.id, json, { headers: this.headers });
  }
}
