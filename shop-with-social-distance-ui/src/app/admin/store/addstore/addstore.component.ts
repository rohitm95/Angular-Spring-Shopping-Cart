import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Inject, Input } from '@angular/core';
import { StoreService } from 'src/app/shared/store.service';
import { ToastrService } from 'ngx-toastr';
import { Store } from 'src/app/models/Store';
import { BreakTiming } from 'src/app/models/BreakTiming';
import { StoreHoliday } from 'src/app/models/StoreHoliday';
import { StoreTiming } from 'src/app/models/StoreTiming';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-addstore',
  templateUrl: './addstore.component.html',
  styleUrls: ['./addstore.component.scss']
})
export class AddstoreComponent implements OnInit {

  @Input() selectedTabIndex
  store: Store = new Store(
    '', '30', 1, this.getDefaultStoreTimings(),
    this.getDefaultBreakTiming(), this.getDefaultStoreHolidays()
  );
  timings = [];
  public isValid: boolean = false;
  constructor(
    private storeService: StoreService,
    private toastr: ToastrService,
    private spinnerService: NgxSpinnerService
  ) { }

  ngOnInit(): void {
    this.intializedBreakTimings();
    this.setSubmitButtonDisableStatus(undefined, '');
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

  addStore(): void {
    if(this.isPageValid()){

    
    this.spinnerService.show();
    this.storeService.addStore(this.store).subscribe(
      (response) => {
        if (response.statusText === 'SUCCESS') {
          this.spinnerService.hide();
          this.toastr.success('Store Added!');
          this.resetStoreDetails();
          const tabGroup = this.selectedTabIndex;
          tabGroup.selectedIndex = 0;
        } else {
          this.spinnerService.hide();
          this.toastr.error('An Error Ocurred, Try again!');
        }        
      },
      (error: HttpErrorResponse) => {
        this.spinnerService.hide();
        this.toastr.error('Connection Error!');
      }
    );
    }
  }

  resetStoreDetails(): void {
    this.store.storeName = '';
    this.store.storeHolidays = this.getDefaultStoreHolidays();
    this.store.breakTimings = this.getDefaultBreakTiming();
    this.store.storeTimings = this.getDefaultStoreTimings();
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

  getDefaultBreakTiming(): Array<BreakTiming> {
    return [
      new BreakTiming('9:00 AM', '10:00 AM', '')
    ];
  }

  getDefaultStoreHolidays(): Array<StoreHoliday> {
    return [
      new StoreHoliday(new Date(), '')
    ];
  }

  setSubmitButtonDisableStatus(text: string, property: string) {
    this.isValid = true;
    if (text == '' || text == null) {
      this.isValid = false;
    }
    if (this.store.breakTimings.length > 0) {
      const result = this.store.breakTimings.filter(element => element.breakType == '');
      if (result.length > 0) {
        this.isValid = false;
      } else {
        this.checkDuplicateBreakValus(property);
      }
    }
    if (this.store.storeHolidays.length > 0) {
      const result = this.store.storeHolidays.filter(element => element.holiday == '' || element.date == null || element.date.getDate() == null);
      if (result.length > 0) {
        this.isValid = false;
      } else {
        this.checkDuplicateHolidayValus(property);
      }
    }
    if (this.store.storeTimings.length > 0) {
      const result = this.store.storeTimings.filter(element => element.weaklyOff == true);
      if (result.length == 0) {
        this.isValid = false;
      }
    }
    if (this.store.storeName == '' || this.store.slotDuration == '') {
      this.isValid = false;
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
      this.isValid = false;
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
      this.isValid = false;
      if (property == 'date') {
        this.toastr.error('Please remove duplicate date value from holiday section');
      } else if (property == 'holiday') {
        this.toastr.error('Please remove duplicate holiday value from holiday section');
      }
    }
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
}
