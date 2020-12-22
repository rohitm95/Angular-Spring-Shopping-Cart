import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnvService } from './env.service';
import { Inventory, InventoryToAddUpdate } from '../models/inventory.model';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  constructor(private http: HttpClient,
    private env: EnvService) { }

  userToken = sessionStorage.getItem('token');

  headers = new HttpHeaders()
    .set('Authorization', 'Bearer ' + this.userToken)
    .set('Access-Control-Allow-Origin', '*');

  getInventoryItems(categories: string) {
    // send store order 
    if (categories === null) {
      return this.http.get(this.env.apiUrl + '/api/items');
    } else {
      return this.http.get(this.env.apiUrl + '/api/items/category?categories=' + categories);
    }
    //return this.http.get(this.env.apiUrl + '/api/items?page=1&size=10', { headers: this.headers });
  }

  getOrders(status: string = ''): Observable<any> {
    if (status !== '' &&
      (status === 'COMPLETED' || status === 'READY_TO_DELIVER' || status === 'LAPSED' || status === 'INITIATED' || status === 'CANCEL_BY_ADMIN')) {
      return this.http.get(this.env.apiUrl + '/api/orders?status=' + status, { headers: this.headers });
    } else {
      return this.http.get(this.env.apiUrl + '/api/orders', { headers: this.headers });
    }
  }

  cancelOrder(data) {
    return this.http.put<any>(this.env.apiUrl + '/api/orders/cancelOrder', data, { headers: this.headers });
  }

  uploadInventoryData(formData) {
    return this.http.post<any>(this.env.apiUrl + '/api/items/uploadFile', formData, {
      headers: this.headers, reportProgress: true,
      observe: 'events',
    });
  }

  bookOrder(obj) {
    // console.log(this.headers);
    return this.http.post<any>(this.env.apiUrl + '/api/availableSlots/bookOrder', obj, { headers: this.headers });
  }

  updateOrderStatus(orderDto, roleDto) {
    const param = { orderDto, roleDto };
    return this.http.put<any>(this.env.apiUrl + '/api/availableSlots/orderStatus', param, { headers: this.headers });
  }

  getOrderByDate(date) {
    return this.http.get<any>(this.env.apiUrl + '/api/search/' + `null/null/${date}/null`, { headers: this.headers });
  }

  getCategories() {
    return this.http.get<any>(this.env.apiUrl + '/api/items/categories', { headers: this.headers });
  }

  getGroups() {
    return this.http.get<any>(this.env.apiUrl + '/api/items/groups', { headers: this.headers });
  }

  getItemTypes() {
    return ["AFD", "Non AFD"];
  }

  addInventory(formData) {
    return this.http.post<any>(this.env.apiUrl + '/api/items', formData, {headers: this.headers   });
  }

  updateInventory(formData, id: number) {
    return this.http.put<any>(this.env.apiUrl + '/api/items/' + id, formData, { headers: this.headers });
  }

  deleteInventoryData(inventoryId) {
    return this.http.delete(this.env.apiUrl + '/api/items/' + inventoryId, { headers: this.headers });
  }

  getOrdersByCustomerId(customerId: number) {
    return this.http.get<any>(this.env.apiUrl + '/api/orders/customer/' + customerId, { headers: this.headers });
  }

  getOrderDetailsByOrderId(orderId: number) {
    console.log(this.env.apiUrl + '/api/orders/orderDetails/' + orderId);
    return this.http.get<any>(this.env.apiUrl + '/api/orders/orderDetails/' + orderId, { headers: this.headers });
  }
  getInventoryItemsForCustomer(categories: string, pageNo: number, size: number) {

    // send store order for customer    
    if (categories === null || categories === '') {
      return this.http.get(this.env.apiUrl + '/api/items?page=' + pageNo + '&size=' + size, { headers: this.headers });
    }
    else {
      return this.http.get(this.env.apiUrl + '/api/items/category?categories=' + categories + '&page=' + pageNo + '&size=' + size);
    }
  }
  getSearchedItemsForCustomer(categories: string, searchItem: string) {
    if (categories === null || categories === '') {
      return this.http.get(this.env.apiUrl + '/api/items/search?itemName=' + searchItem, { headers: this.headers })
    }
    else {
      return this.http.get(this.env.apiUrl + '/api/items/search?categories=' + categories + '&itemName=' + searchItem);
    }

  }


}
