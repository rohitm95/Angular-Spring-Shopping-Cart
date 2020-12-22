import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { InventoryService } from 'src/app/shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { map, catchError } from 'rxjs/operators';
import { HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import * as XLSX from 'xlsx';
import { NgxSpinnerService } from 'ngx-spinner';

type AOA = any[][];

@Component({
  selector: 'app-inventory-upload',
  templateUrl: './inventory-upload.component.html',
  styleUrls: ['./inventory-upload.component.scss']
})
export class InventoryUploadComponent implements OnInit {

  @ViewChild('fileUpload', { static: false }) fileUpload: ElementRef;
  files = [];
  fileUploadDetails: any;
  successStatus: any;
  data: AOA = [['The data will be shown here']];
  isFileSelected = false;
  isUploading = false;
  errorMessage: string = '';

  constructor(private spinnerService: NgxSpinnerService,
    public dialogRef: MatDialogRef<InventoryUploadComponent>,
    private inventoryService: InventoryService, private toastr: ToastrService) {
    this.errorMessage = '';
  }


  ngOnInit(): void {
    this.errorMessage = '';
  }

  validateInventoryFileHeaders(rows): boolean {
    let isValid = true;
    if (rows[0].length !== 14) {
      isValid = false;
      return isValid;
    }
    if (isValid) {
      if (rows[0][0] != 'Item Number') {
        isValid = false;
      }
      else if (rows[0][1] != 'Group') {
        isValid = false;
      }
      else if (rows[0][2] != 'Name') {
        isValid = false;
      }
      else if (rows[0][3] != 'Price') {
        isValid = false;
      }
      else if (rows[0][4] != 'Stock') {
        isValid = false;
      }
      else if (rows[0][5] != 'Category') {
        isValid = false;
      }
      else if (rows[0][6] != 'Low Stock Indicator') {
        isValid = false;
      }
      else if (rows[0][7] != 'To be Sold one item per order (Yes/No)') {
        isValid = false;
      }
      else if (rows[0][8] != 'Weight/ Volume per Item') {
        isValid = false;
      }
      else if (rows[0][9] != 'Whether In Stock (Yes/No)') {
        isValid = false;
      }
      else if (rows[0][10] != 'Monthly Quota per User per Item') {
        isValid = false;
      }
      else if (rows[0][11] != 'ItemType (AFD/ Non AFD)') {
        isValid = false;
      }
      else if (rows[0][12] != 'Yearly Quota Per User Per Item') {
        isValid = false;
      }
      else if (rows[0][13] != 'Image_path') {
        isValid = false;
      }
      else {
        isValid = true;
      }
    }
    return isValid;
  }

  validateInventoryFileValues(rows): string { ///^[.\d]+$/
    let errorMessage = '<ul>';
    let errorArray = [];

    rows.forEach((value, index) => {
      if (index > 0 && value && value.length > 0) {

        if (!value[0]) {
          errorArray.push({ 'column': 'ItemNumber', 'LineNo': index });
        }
        if (!value[1]) {
          errorArray.push({ 'column': 'Group', 'LineNo': index });
        }
        if (!value[2]) {
          errorArray.push({ 'column': 'Name', 'LineNo': index });
        }
        if (!value[3]) { //price
          errorArray.push({ 'column': 'Price', 'LineNo': index });
        }
        else if (value[3] && isNaN(value[3]) || value[3] < 0) {
          errorArray.push({ 'column': 'PriceValid', 'LineNo': index });
        } else if (!isNaN(value[3]) && parseInt(value[3]) < 0) {
          errorArray.push({ 'column': 'PriceValid', 'LineNo': index });
        }
        if (!value[4]) {
          errorArray.push({ 'column': 'Stock', 'LineNo': index });
        }
        else if (value[4] && isNaN(value[4])) {
          errorArray.push({ 'column': 'StockValid', 'LineNo': index });
        } else if (value[4] && !isNaN(value[4]) && parseInt(value[4]) < 0) {
          errorArray.push({ 'column': 'StockValid', 'LineNo': index });
        }
        if (!value[5]) {
          errorArray.push({ 'column': 'Category', 'LineNo': index });
        }
        if (!value[6]) {
          errorArray.push({ 'column': 'LowStockIndicator', 'LineNo': index });
        }
        else if (value[6] && isNaN(value[6])) {
          errorArray.push({ 'column': 'LowStockIndicatorValid', 'LineNo': index });
        } else if (value[6] && !isNaN(value[6]) && parseInt(value[4]) < 0) {
          errorArray.push({ 'column': 'LowStockIndicatorValid', 'LineNo': index });
        }
        if (!value[7]) {
          errorArray.push({ 'column': 'ToBeSold', 'LineNo': index });
        }
        else if (value[7] && value[7] != 'Yes' && value[7] != 'No') {
          errorArray.push({ 'column': 'ToBeSoldValid', 'LineNo': index });
        }
        // if (!value[8]) {
        //   errorArray.push({ 'WeightVolume': '', 'LineNo': index });
        // }
        if (!value[9]) {
          errorArray.push({ 'InStock': '', 'LineNo': index });
        }
        else if (value[9] && value[9] != 'Yes' && value[9] != 'No') {
          errorArray.push({ 'column': 'InStockValid', 'LineNo': index });
        }
        if (!value[10]) {
          errorArray.push({ 'MonthlyQuota': '', 'LineNo': index });
        }
        else if (value[10] && isNaN(value[10])) {
          errorArray.push({ 'column': 'MonthlyQuotaValid', 'LineNo': index });
        } else if (value[10] && !isNaN(value[10]) && parseInt(value[10]) < 0) {
          errorArray.push({ 'column': 'MonthlyQuotaValid', 'LineNo': index });
        }
        if (!value[11]) {
          errorArray.push({ 'ItemType': '', 'LineNo': index });
        }
        else if (value[11] && value[11] != 'AFD' && value[11] != 'Non AFD') {
          errorArray.push({ 'column': 'ItemType', 'LineNo': index });
        }
        if (!value[12]) {
          errorArray.push({ 'YearlyQuota': '', 'LineNo': index });
        }
        if (!value[13]) {
          errorArray.push({ 'column': 'Image', 'LineNo': index });
        }
      }
    });

    if (errorArray.length > 0) {
      errorMessage += this.getErrorIndexArray(errorArray, 'ItemNumber');
      errorMessage += this.getErrorIndexArray(errorArray, 'Group');
      errorMessage += this.getErrorIndexArray(errorArray, 'Name');
      errorMessage += this.getErrorIndexArray(errorArray, 'Price');
      errorMessage += this.getErrorIndexArray(errorArray, 'PriceValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'Stock');
      errorMessage += this.getErrorIndexArray(errorArray, 'StockValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'Category');
      errorMessage += this.getErrorIndexArray(errorArray, 'LowStockIndicator');
      errorMessage += this.getErrorIndexArray(errorArray, 'LowStockIndicatorValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'ToBeSold');
      errorMessage += this.getErrorIndexArray(errorArray, 'ToBeSoldValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'InStock');
      errorMessage += this.getErrorIndexArray(errorArray, 'InStockValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'MonthlyQuota');
      errorMessage += this.getErrorIndexArray(errorArray, 'MonthlyQuotaValid');
      errorMessage += this.getErrorIndexArray(errorArray, 'ItemType');
      errorMessage += this.getErrorIndexArray(errorArray, 'YearlyQuota');
      errorMessage += this.getErrorIndexArray(errorArray, 'Image');
    }
    errorMessage += '</ul>';
    return errorMessage;
  }

  getErrorIndexArray(errorArray, column) {
    let filteredErrorArray = [];
    let indexes = [];

    if (errorArray.length > 0) {
      filteredErrorArray = errorArray.filter((value) => {
        return value.column == column;
      });
      if (filteredErrorArray.length > 0) {
        filteredErrorArray.forEach(function (value) {
          indexes.push(value.LineNo);
        });
      }
      return this.getErrorMessage(indexes, column);
    }
  }

  getErrorMessage(indexes, column) {
    let errorMessage = '';
    if (indexes.length > 0) {
      switch (column) {
        case 'ItemNumber':
          errorMessage = '<li>Item Number Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Group':
          errorMessage = '<li>Group Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Name':
          errorMessage = '<li>Name Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Price':
          errorMessage = '<li>Price Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'PriceValid':
          errorMessage = '<li>Price Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Stock':
          errorMessage = '<li>Stock Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'StockValid':
          errorMessage = '<li>Stock Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Category':
          errorMessage = '<li>Category Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'LowStockIndicator':
          errorMessage = '<li>Low Stock Indicator Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'LowStockIndicatorValid':
          errorMessage = '<li>Low Stock Indicator Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'ToBeSold':
          errorMessage = '<li>To be Sold one item per order (Yes/No) Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'ToBeSoldValid':
          errorMessage = '<li>To be Sold one item per order (Yes/No) Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'InStock':
          errorMessage = '<li>Whether In Stock (Yes/No) Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'InStockValid':
          errorMessage = '<li>Whether In Stock (Yes/No) Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'MonthlyQuota':
          errorMessage = '<li>Monthly Quota per User per Item Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'MonthlyQuotaValid':
          errorMessage = '<li>Monthly Quota per User per Item Is Invalid In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'ItemType':
          errorMessage = '<li>ItemType (AFD/ Non AFD) Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'YearlyQuota':
          errorMessage = '<li>Yearly Quota Per User Per Item Is Required In Line No ' + indexes.join(', ') + '</li>';
          break;
        case 'Image':
          errorMessage = '<li>Image is required In Line No' + indexes.join(', ') + '</li>';
          break;
      }
    }
    return errorMessage;
  }

  onFileChange(evt: any) {
    /* wire up file reader */
    const target: DataTransfer = evt.target as DataTransfer;
    if (target.files.length !== 1) {
      throw new Error('Cannot use multiple files');
    }
    const reader: FileReader = new FileReader();
    reader.onload = (e: any) => {
      /* read workbook */
      const bstr: string = e.target.result;
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' });

      /* grab first sheet */
      const wsname: string = wb.SheetNames[0];
      const ws: XLSX.WorkSheet = wb.Sheets[wsname];

      /* save data */
      this.data = XLSX.utils.sheet_to_json(ws, { header: 1 }) as AOA;

      if (this.data.length === 0) {
        this.data = [[]];
        this.errorMessage = 'No Records For Upload';
      }
      else {
        if (!this.validateInventoryFileHeaders(this.data)) {
          this.data = [[]];
          this.errorMessage = 'Invalid header';
        }
        else {
          this.errorMessage = this.validateInventoryFileValues(this.data);
          if (this.errorMessage != '<ul></ul>') {
            this.data = [[]];
          }
        }
      }
    };
    reader.readAsBinaryString(target.files[0]);
    this.isFileSelected = true;
  }

  uploadFile(file) {
    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', file.data);
    file.inProgress = true;
    this.spinnerService.show();
    this.inventoryService
    .uploadInventoryData(formData)
      .pipe(
        map((event: any) => {
          switch (event.type) {
            case HttpEventType.UploadProgress:
              file.progress = Math.round((event.loaded * 100) / event.total);
              break;
            case HttpEventType.Response:
              return event;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          file.inProgress = false;
          this.toastr.error('Uploading Failed');
          return of(`${file.data.name} upload failed.`);
        })
      )
      .subscribe((event: any) => {
        if (typeof event === 'object') {
          // console.log(event.body);
          // console.log(event.body.statusText);
          this.successStatus = event.body.statusText;
          if (this.successStatus === 'SUCCESS') {
            this.toastr.success('Uploaded Successfully!');
            this.dialogRef.close();
          }
        }
        this.spinnerService.hide();
      },
        error => {
          this.toastr.error('Error in Uploading File!');
          this.dialogRef.close();
          this.spinnerService.hide();
        });
  }

  onClick() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      // for (let index = 0; index < fileUpload.files.length; index++) {
      const file = fileUpload.files[0];

      this.files.push({ data: file, inProgress: false, progress: 0, name: file.name });
      // }
    };
    fileUpload.click();
  }

  submitFile() {
    this.uploadFile(this.files[0]);
  }

}
