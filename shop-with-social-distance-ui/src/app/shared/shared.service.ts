import { Injectable } from '@angular/core';
import { Subject } from 'rxjs'
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  constructor(private router: Router) { }

  getFormattedDate(dateToFormat): string {
    if(!dateToFormat){
      return '';
    }
    //let timeZoneOffset = new Date().getTimezoneOffset() * 60 * 1000;
    //let localDate = dateToFormat + timeZoneOffset;
    let localDate=new Date(dateToFormat);
    let formatted_date = '';
    let todate = new Date(localDate).getDate();
    let tomonth = new Date(localDate).getMonth() + 1;
    let toyear = new Date(localDate).getFullYear();
    if (todate < 10) {
      formatted_date = toyear + '-' + tomonth + '-' + '0' + todate;
    }
    else if (tomonth < 10) {
      formatted_date = toyear + '-0' + tomonth + '-' + todate;
    }
    else {
      formatted_date = toyear + '-' + tomonth + '-' + todate;
    }
    return formatted_date;
  }

  getFormatedStatus(status): string {
    let formattedStatus: string = '';
    switch (status) {
      case 'INITIATED':
        formattedStatus = 'PENDING';
        break;
      case 'PENDING':
        formattedStatus = 'PENDING';
        break;
      case 'READY_TO_DELIVER':
        formattedStatus = 'READY';
        break;
      case 'LAPSED':
        formattedStatus = 'LAPSED';
        break;
      case 'COMPLETED':
        formattedStatus = 'COMPLETED';
        break;
      case 'CANCEL_BY_ADMIN':
        formattedStatus = 'CANCELLED';
        break;
      case 'CANCEL_BY_AGGREGATOR':
        formattedStatus = 'CANCELLED';
        break;
    }
    return formattedStatus;
  }

  getFormattedSlot(slotFrom: string, slotTo: string): string {
    if (!slotFrom) {
      slotFrom = '';
    }
    if (!slotTo) {
      slotTo = '';
    }
    return slotFrom + ' - ' + slotTo;
  }

  compareDates(date_val: string, date_fval: string): boolean {
    let returnValue: boolean = false;
    let fval_array = date_fval.split('/');
    let year: string = fval_array[2];
    let day: string = fval_array[1];
    let month: string = fval_array[0];
    let n_month: number = parseInt(month);
    let n_day: number = parseInt(day);
    month = n_month < 10 ? + '0' + n_month.toString() : n_month.toString();
    day = n_day < 10 ? + '0' + n_day.toString() : n_day.toString();

    let fval = year + '-' + month + '-' + day;
    if (date_val == fval) {
      returnValue = true;
    }

    return returnValue;
  }

  navigateToUsetLandingPageByRole(): void {
    const role = sessionStorage.getItem('userRole');
    switch (role.toLowerCase()) {
      case 'admin':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'customer':
        this.router.navigate(['/customer/product-list']);
        break;
      case 'aggregator':
        this.router.navigate(['/aggregator/orders']);
        break;
    }
  }

  timeConvert12to24(time1: any): any {
    let time = time1.toLowerCase();
    let hours = Number(time.match(/^(\d+)/)[1]);
    let minutes = Number(time.match(/:(\d+)/)[1]);
    let AMPM = time.match(/\s(.*)$/)[1];
    if (AMPM === 'pm' && hours < 12) {
      hours = hours + 12;
    }
    if (AMPM === 'am' && hours === 12) {
      hours = hours;
    }
    let sHours = hours.toString();
    let sMinutes = minutes.toString();
    if (hours < 10) {
      sHours = '0' + hours;
    }
    if (minutes < 10) {
      sMinutes = '0' + minutes;
    }
    let res = (sHours + ':' + sMinutes);
    return res;
  }

  getFormatedCustomerName(firstName: string, lastName: string): string {
    return lastName === null || lastName === ''
      ? firstName
      : firstName + ' ' + lastName;
  }

  getFormatedOrderObject(order) {
    let obj = {
      orderNo: order.order_no.toString(),
      delivery_date: this.getFormattedDate(order.deliveryDate),
      time_slot: this.getFormattedSlot(order.slotFrom, order.slotTo),
      customer: order.customer,
      status: order.status,
      amountPayable: order.amountPayable,
      store: order.store,
      inventoryDatas: order.inventoryDatas,
      customer_name: this.getFormatedCustomerName(order.customer.firstName, order.customer.lastName),
      cancelBy: order.cancelBy,
      cancelRemark: order.cancelRemark,
      deliveryDate: order.deliveryDate,
      order_no: order.order_no,
      slotFrom: order.slotFrom,
      slotTo: order.slotTo,
      order_status: this.getFormatedStatus(order.status)
    };
    return obj;
  }
}