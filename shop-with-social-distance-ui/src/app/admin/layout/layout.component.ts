import { Component, OnInit, ViewChild } from '@angular/core';
import { OrdersComponent } from '../orders/orders.component';
import { DashboardComponent } from '../dashboard/dashboard.component';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  orderStatusFromDashBoard: string = '';

  constructor() {
    this.orderStatusFromDashBoard = '';
  }

  ngOnInit(): void {
  }

  onOrderStatusFromDashBoard(orderStatusFromDashBoard) {
    //this.orderStatusFromDashBoard = status;

    //this.orderComponent.orderStatusFromDashBoard = this.orderStatusFromDashBoard;
    this.orderStatusFromDashBoard = orderStatusFromDashBoard;
  }
}
