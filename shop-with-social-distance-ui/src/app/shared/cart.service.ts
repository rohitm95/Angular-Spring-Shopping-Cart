import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';
import { EnvService } from './env.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  selectedItems = [];

  constructor(private http: HttpClient, private userService: UserService, private env: EnvService) { }

  getTimeSlots(data: any) {
    return this.http.get(this.env.apiUrl + '/api/availableSlots', {
      params: data
    });
  }
}
