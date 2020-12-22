import { Component, OnInit, Inject } from '@angular/core';
import { CartService } from 'src/app/shared/cart.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../shared/user.service';
import { InventoryService } from '../../shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedService } from 'src/app/shared/shared.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {

  // checkoutForm: FormGroup;
  timeSlots;
  cartProductList;
  amountPayable;
  user;
  slotFrom;
  slotTo;
  deliveryDate;
  status = 'INITIATED';
  inventoryDatas = [];
  isClicked = false;
  isPurchaseLimitAvailable = true;
  limitExceedMessage = '';

  constructor(private cartService: CartService,
    private inventoryService: InventoryService,
    private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<CheckoutComponent>,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService) { }

  ngOnInit(): void {

    this.cartProductList = this.data.cartProductList;
    this.amountPayable = this.data.totalAmount;
    this.user = JSON.parse(sessionStorage.getItem('user'));
    // console.log(this.UserService.user);
    //console.log(this.user);

    this.cartProductList.forEach(element => {
      this.inventoryDatas.push(element.product);
    });
    this.getTimeslots();
  }

  selectTimeSlot(slot, time) {
    let selectTime = slot.split('-');
    //console.log(selectTime[0], selectTime[1]);
    this.slotFrom = this.sharedService.timeConvert12to24(selectTime[0]).toString();
    this.slotTo = this.sharedService.timeConvert12to24(selectTime[1]).toString();
    this.deliveryDate = new Date(time).getTime();
  }

  getTimeslots() {
    this.spinnerService.show();
    this.cartService.getTimeSlots({
      userId: this.user.id,
      amountPayable: this.amountPayable
    }).subscribe((res: any) => {
      if (res.status === 400) {
        this.isPurchaseLimitAvailable = false;
        this.limitExceedMessage = res.statusText;
        this.spinnerService.hide();
      } else {
        this.timeSlots = res.result;
        this.spinnerService.hide();
      }
    },
      error => {
        this.toastr.error('Connection Error!');
        this.spinnerService.hide();
      });
  }

  validateOrder(): boolean {
    if (!this.deliveryDate) {
      this.toastr.error('Please select timeslot');
      return false;
    }
    else if (!this.slotFrom) {
      this.toastr.error('Please select timeslot');
      return false;
    }
    else if (!this.slotTo) {
      this.toastr.error('Please select timeslot');
      return false;
    }
    return true;
  }

  bookOrder() {

    if (this.validateOrder()) {


      let bookingData = {
        customer: this.user,
        deliveryDate: this.deliveryDate,
        slotFrom: this.slotFrom,
        slotTo: this.slotTo,
        amountPayable: this.amountPayable,
        status: this.status,
        inventoryDatas: this.inventoryDatas
      };
      this.isClicked = true;
      this.spinnerService.show();
      this.inventoryService.bookOrder(bookingData).subscribe(
        (res: any) => {
          if (res != null) {
            this.dialogRef.close('Done');
            this.toastr.success('Your order ' + '#' + res.result.orderNo + ' is placed , pls check your registered mail for your order updates.');
          } else {
            this.dialogRef.close();
            this.toastr.error('Server not responding, please login and try again');
          }
          this.spinnerService.hide();
        },
        error => {
          this.toastr.error('Order not placed, please try again!');
          this.dialogRef.close();
          this.spinnerService.hide();
        });
    }
  }
}
