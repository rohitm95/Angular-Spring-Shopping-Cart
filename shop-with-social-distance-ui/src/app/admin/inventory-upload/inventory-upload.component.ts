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
  constructor(private spinnerService: NgxSpinnerService,
    public dialogRef: MatDialogRef<InventoryUploadComponent>,
    private inventoryService: InventoryService, private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  validateInventoryFileHeaders(rows): boolean {
    let isValid = true;
    if (rows[0].length !== 13) {
      isValid = false;
      return isValid;
    }

    rows[0].forEach((value) => {
      switch (value) {
        case 'Item Number':
        case 'Group':
        case 'Name':
        case 'Price':
        case 'Stock':
        case 'Category':
        case 'Low Stock Indicator':
        case 'To be Sold one item per order (Yes/No)':
        case 'Weight/ Volume per Item':
        case 'Whether In Stock (Yes/No)':
        case 'Monthly Quota per User per Item':
        case 'ItemType (AFD/ Non AFD)':
        case 'Yearly Quota Per User Per Item':
          isValid = true;
          break;
        default:
          isValid = false;
          return false;
      }
    });

    return isValid;
  }

  validateInventoryFileValues(rows): boolean { ///^[.\d]+$/
    let isValid = true;
    rows.forEach((value, index) => {
      debugger;
      if (index > 0 && value && value.length > 0) {
        if (!value[3] || isNaN(value[3]) || value[3] < 0) { //price
          isValid = false;
          return false;
        }
        if (!value[4] || isNaN(value[4]) || value[4] < 0) { //stock
          isValid = false;
          return false;
        }
        if (!value[6] || isNaN(value[6]) || value[6] < 0) { //low stock indicator
          isValid = false;
          return false;
        }
        if (!value[10] || isNaN(value[10]) || value[10] < 0) { //monthly quota 
          isValid = false;
          return false;
        }
      }
    });
    return isValid;
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
        this.data = [['No records to upload']];
      }
      else if (!this.validateInventoryFileHeaders(this.data)) {
        this.data = [['Invalid template headers']];
      }
      else if (!this.validateInventoryFileValues(this.data)) {
        this.data = [['Invalid records']];
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
    // this.inventoryService
    //   .uploadInventoryData(formData)
    //   .pipe(
    //     map((event: any) => {
    //       switch (event.type) {
    //         case HttpEventType.UploadProgress:
    //           file.progress = Math.round((event.loaded * 100) / event.total);
    //           break;
    //         case HttpEventType.Response:
    //           return event;
    //       }
    //     }),
    //     catchError((error: HttpErrorResponse) => {
    //       file.inProgress = false;
    //       this.toastr.error('Uploading Failed');
    //       return of(`${file.data.name} upload failed.`);
    //     })
    //   )
    //   .subscribe((event: any) => {
    //     if (typeof event === 'object') {
    //       // console.log(event.body);
    //       // console.log(event.body.statusText);
    //       this.successStatus = event.body.statusText;
    //       if (this.successStatus === 'SUCCESS') {
    //         this.toastr.success('Uploaded Successfully!');
    //         this.dialogRef.close();
    //       }
    //     }
    //     this.spinnerService.hide();
    //   },
    //     error => {
    //       this.toastr.error('Error in Uploading File!');
    //       this.dialogRef.close();
    //       this.spinnerService.hide();
    //     });
  }

  onClick() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      console.log(fileUpload);
      // for (let index = 0; index < fileUpload.files.length; index++) {
      const file = fileUpload.files[0];
      console.log(file.name);
      this.files.push({ data: file, inProgress: false, progress: 0, name: file.name });
      // }
    };
    fileUpload.click();
  }

  submitFile() {
    this.uploadFile(this.files[0]);
  }

}
