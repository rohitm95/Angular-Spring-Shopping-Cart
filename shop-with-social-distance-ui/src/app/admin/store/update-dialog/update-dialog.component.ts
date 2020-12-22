import { HttpErrorResponse } from '@angular/common/http';
import { StoreService } from './../../../shared/store.service';
import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UpdatestoreComponent } from '../updatestore/updatestore.component';
import { StoreTiming } from 'src/app/models/StoreTiming';
import { BreakTiming } from 'src/app/models/BreakTiming';
import { StoreHoliday } from 'src/app/models/StoreHoliday';
import { Store } from 'src/app/models/Store';
import { ToastrService } from 'ngx-toastr';
import { element } from 'protractor';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-update-dialog',
  templateUrl: './update-dialog.component.html',
  styleUrls: ['./update-dialog.component.scss']
})
export class UpdateDialogComponent implements OnInit {

  store: Store;
  timings = [];
  public isValid: boolean = true;

  constructor(
    private storeService: StoreService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<UpdatestoreComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private spinnerService: NgxSpinnerService
  ) {
    this.store = data.store;
    this.isValid = true;
  }

  ngOnInit(): void {
    this.intializedBreakTimings();
    if (this.store.storeTimings.length == 0) {
      this.store.storeTimings = this.getDefaultStoreTimings();
    }
    this.setSubmitButtonDisableStatus(undefined, '');
  }

  getDefaultStoreTimings(): Array<StoreTiming> {
    return [
      new StoreTiming('SUNDAY', '9:00 AM', '7:30 PM', true),
      new StoreTiming('MONDAY', '9:00 AM', '7:30 PM', false),
      new StoreTiming('TUESDAY', '9:00 AM', '7:30 PM', false),
      new StoreTiming('WEDNESDAY', '9:00 AM', '7:30 PM', false),
      new StoreTiming('THURSDAY', '9:00 AM', '7:30 PM', false),
      new StoreTiming('FRIDAY', '9:00 AM', '7:30 PM', false),
      new StoreTiming('SATURDAY', '9:00 AM', '7:30 PM', false)
    ];
  }

  intializedBreakTimings(): void {
    this.intializeBreakHour(0, 'AM');
    for (let i = 1; i < 12; i++) {
      this.intializeBreakHour(i, 'AM');
    }

    this.intializeBreakHour(12, 'PM');
    for (let i = 1; i < 12; i++) {
      this.intializeBreakHour(i, 'PM');
    }
  }

  intializeBreakHour(hour: number, amOrPm: string): void {
    this.timings.push(`${hour}:00 ${amOrPm}`);
    this.timings.push(`${hour}:15 ${amOrPm}`);
    this.timings.push(`${hour}:30 ${amOrPm}`);
    this.timings.push(`${hour}:45 ${amOrPm}`);
  }

  addBreak() {
    this.isValid = true;
    if (this.store.breakTimings.length > 0) {
      if (this.store.breakTimings[this.store.breakTimings.length - 1].breakType !== '') {
        this.store.breakTimings.push(new BreakTiming('9:00 AM', '10:00 AM', ''));
      } else {
        this.toastr.warning('Please enter previous break name');
      }
    } else {
      this.store.breakTimings.push(new BreakTiming('9:00 AM', '10:00 AM', ''));
    }
  }

  addHoliday() {
    this.isValid = true;
    if (this.store.storeHolidays.length > 0) {
      if (this.store.storeHolidays[this.store.storeHolidays.length - 1].holiday !== '') {
        this.store.storeHolidays.push(new StoreHoliday(null, ''));
      } else {
        this.toastr.warning('Please enter previous holiday');
      }
    } else {
      this.store.storeHolidays.push(new StoreHoliday(null, ''));
    }
  }

  saveStore() {
    if (this.isPageValid()) {
      this.spinnerService.show();
      this.storeService.updateStore(this.store).subscribe(
        (response: any) => {
          this.spinnerService.hide();
          if (response.statusText === 'SUCCESS') {
            this.dialogRef.close();
            this.toastr.success('Store Details Updated!!');
          } else {
            this.toastr.error(response.statusText);
          }
        },
        (error: HttpErrorResponse) => {
          this.spinnerService.hide();
          this.toastr.error('Connection Error!');
        }
      );
    }
  }

  isPageValid(): boolean {
    let isValid: boolean = true;
    let msg: string = '';
    if (this.store.breakTimings.length > 0) {
      const result = this.store.breakTimings.filter(element => element.breakType == '');
      if (result.length > 0) {
        msg = 'Please enter all values in break section.';
        isValid = false;
      } else {
        var sorted_arr_break = this.store.breakTimings.slice().sort();
        var results = [];
        for (var i = 0; i < sorted_arr_break.length - 1; i++) {
          if (sorted_arr_break[i + 1]['breakFrom'].toLowerCase().trim() === sorted_arr_break[i]['breakFrom'].toLowerCase().trim() ||
            sorted_arr_break[i + 1]['breakTo'].toLowerCase().trim() === sorted_arr_break[i]['breakTo'].toLowerCase().trim() ||
            sorted_arr_break[i + 1]['breakType'].toLowerCase().trim() === sorted_arr_break[i]['breakType'].toLowerCase().trim()
          ) {
            results.push(sorted_arr_break[i]);
          }
        }
        if (results.length > 0) {
          msg += 'Please remove duplicate values in break section.';
          isValid = false;
        }
      }
    }
    if (this.store.storeHolidays.length > 0) {
      const result = this.store.storeHolidays.filter(element => element.holiday == '' || element.date == null);
      if (result.length > 0) {
        msg = '\nPlease enter all values in holiday section.';
        isValid = false;
      } else {
        var sorted_arr_holiday = this.store.storeHolidays.slice().sort();
        var results = [];
        for (var i = 0; i < sorted_arr_holiday.length - 1; i++) {
          if (sorted_arr_holiday[i + 1]['date'].getDate() === sorted_arr_holiday[i]['date'].getDate() ||
            sorted_arr_holiday[i + 1]['holiday'].toLowerCase().trim() === sorted_arr_holiday[i]['holiday'].toLowerCase().trim()
          ) {
            results.push(sorted_arr_holiday[i]);
          }
        }
        if (results.length > 0) {
          isValid = false;
          msg += 'Please remove duplicate value from holiday section';
        }
      }
    }
    if (msg != '') {
      this.toastr.error(msg);
    }
    return isValid;
  }

  cancel(): void {
    this.dialogRef.close();
  }

  removeBreak(index) {
    this.store.breakTimings.splice(index, 1);
    if (this.store.breakTimings.length == 0) {
      this.isValid = false;
    }
    this.setSubmitButtonDisableStatus(undefined, '');
  }

  removeHoliday(index) {
    this.store.storeHolidays.splice(index, 1);
    if (this.store.storeHolidays.length == 0) {
      this.isValid = false;
    }
    this.setSubmitButtonDisableStatus(undefined, '');
  }

  setSubmitButtonDisableStatus(text: string, property: string) {
    this.isValid = false;
    if (text == '') {
      this.isValid = true;
    }
    if (this.store.breakTimings.length > 0) {
      const result = this.store.breakTimings.filter(element => element.breakType == '');
      if (result.length > 0) {
        this.isValid = true;
      } else {
        this.checkDuplicateBreakValus(property);
      }
    }
    if (this.store.storeHolidays.length > 0) {
      const result = this.store.storeHolidays.filter(element => element.holiday == '' || element.date == null);
      if (result.length > 0) {
        this.isValid = true;
      } else {
        this.checkDuplicateHolidayValus(property);
      }
    }
    if (this.store.storeTimings.length > 0) {
      const result = this.store.storeTimings.filter(element => element.weaklyOff == true);
      if (result.length == 0) {
        this.isValid = true;
      }
    }
    if (this.store.storeName == '' || this.store.slotDuration == '') {
      this.isValid = true;
    }
  }

  checkDuplicateBreakValus(property: string) {
    var sorted_arr_break = this.store.breakTimings.slice().sort();
    var results = [];
    for (var i = 0; i < sorted_arr_break.length - 1; i++) {
      if (sorted_arr_break[i + 1][property].toLowerCase().trim() === sorted_arr_break[i][property].toLowerCase().trim()) {
        results.push(sorted_arr_break[i]);
      }
    }
    if (results.length > 0) {
      this.isValid = true;
      if (property == 'breakFrom') {
        this.toastr.error('Please remove duplicate break from value from break section');
      } else if (property == 'breakTo') {
        this.toastr.error('Please remove duplicate break to value from break section');
      } else if (property == 'breakType') {
        this.toastr.error('Please remove duplicate break name value from break section');
      }
    }
  }

  checkDuplicateHolidayValus(property: string) {
    var sorted_arr_holiday = this.store.storeHolidays.slice().sort();
    var results = [];
    for (var i = 0; i < sorted_arr_holiday.length - 1; i++) {
      if (property == 'date') {
        if (sorted_arr_holiday[i + 1][property].getDate() === sorted_arr_holiday[i][property].getDate()) {
          results.push(sorted_arr_holiday[i]);
        }
      }
      else if (property == 'holiday') {
        if (sorted_arr_holiday[i + 1][property].toLowerCase().trim() === sorted_arr_holiday[i][property].toLowerCase().trim()) {
          results.push(sorted_arr_holiday[i]);
        }
      }
    }
    if (results.length > 0) {
      this.isValid = true;
      if (property == 'date') {
        this.toastr.error('Please remove duplicate date value from holiday section');
      } else if (property == 'holiday') {
        this.toastr.error('Please remove duplicate holiday value from holiday section');
      }
    }
  }
}