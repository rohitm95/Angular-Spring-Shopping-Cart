import { Component, OnInit, ViewChild, ElementRef, Inject } from '@angular/core';
import { InventoryService } from 'src/app/shared/inventory.service';
import { ToastrService } from 'ngx-toastr';
import { map, catchError } from 'rxjs/operators';
import { HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as XLSX from 'xlsx';
import { CustomerService } from 'src/app/shared/customer.service';
import { NgxSpinnerService } from 'ngx-spinner';

type AOA = any[][];

@Component({
  selector: 'app-customer-upload',
  templateUrl: './customer-upload.component.html',
  styleUrls: ['./customer-upload.component.scss']
})
export class CustomerUploadComponent implements OnInit {

  @ViewChild('fileUpload', { static: false }) fileUpload: ElementRef;
  files = [];
  successStatus: any;
  data: AOA = [['The data will be shown here']];
  isFileSelected = false;
  errorMessage: string;
  constructor(public dialogRef: MatDialogRef<CustomerUploadComponent>,
    @Inject(MAT_DIALOG_DATA) public customers: any,
    private inventoryService: InventoryService,
    private toastr: ToastrService,
    private customerService: CustomerService,
    private spinnerService: NgxSpinnerService) {
    this.errorMessage = '';
  }

  ngOnInit(): void {
    this.errorMessage = '';
  }

  validateUserFile(uploadedFiles): boolean {
    if (uploadedFiles.length !== 1) {
      this.toastr.error('Cannot use multiple files');
      return false;
    }
    if (uploadedFiles[0].type !== 'application/vnd.ms-excel' &&
      uploadedFiles[0].type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
      this.toastr.error('Invalid file');
      return false;
    }
    return true;
  }

  validateUserFileHeader(rows): boolean {
    let isValid = true;
    if (rows[0].length !== 9) {
      isValid = false;
    }
    if (isValid) {
      rows[0].forEach((value, index) => {
        if (index == 0) {
          switch (value) {
            case 'Username':
            case 'Mobile Number':
            case 'Name':
            case 'Role Id':
            case 'Activate/ Deactivate User':
            case 'Category':
            case 'AFD Item Purchase Limit Per Year':
            case 'Non AFD Item Purchase Limit Per Month':
            case 'EmailID':
              isValid = true;
              break;
            default:
              isValid = false;
              return false;
          }
        }
        else {
          return;
        }
      });
    }
    return isValid;
  }

  validateUserFileValues(rows): string {
    let errorMessage = '<ul>';

    let emailPattern: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    let mobNumberPattern: RegExp = /^((\\+91-?)|0)?[0-9]{10}$/

    rows.forEach((value, index) => {
      if (index > 0 && value && value.length > 0) {
        if (!value[0]) {
          errorMessage += '<li>User Name Is Required In Line # ' + index + '</li>';
        }
        if (!value[1]) {
          errorMessage += '<li>Mobile Number Is Required In Line # ' + index + '</li>';
        }
        else if (value[1] && !value[1].match(mobNumberPattern)) {
          errorMessage += '<li>Invalid Mobile Number In Line # ' + index + '</li>';
        }
        if (!value[2]) {
          errorMessage += '<li>Name Is Required In Line # ' + index + '</li>';
        }
        if (!value[3]) {
          errorMessage += '<li>Role Is Required In Line # ' + index + '</li>';
        }
        if (!value[4]) {
          errorMessage += '<li>User Status Is Required In Line # ' + index + '</li>';
        }
        else if (value[4] != "Activate" && value[3] != "Inactivate") {
          errorMessage += '<li>Invalid User Status In Line # ' + index + '</li>';
        }
        if (!value[5]) {
          errorMessage += '<li>Category Is Required In Line # ' + index + '</li>';
        }
        else if (value[5] != "A" && value[5] != "B" && value[5] != "C") {
          errorMessage += '<li>Invalid Category In Line # ' + index + '</li>';
        }
        if (!value[6]) {
          errorMessage += '<li>AFD Item Purchase Limit Per Year Is Required In Line # ' + index + '</li>';
        }
        else if (value[6] && isNaN(value[6]) && parseInt(value[6]) > 0) {
          errorMessage += '<li>Invalid AFD Item Purchase Limit Per Year In Line # ' + index + '</li>';
        }
        if (!value[7]) {
          errorMessage += '<li>Non AFD Item Purchase Limit Per Month Is Required In Line # ' + index + '</li>';
        }
        else if (value[7] && isNaN(value[7]) && parseInt(value[7]) > 0) {
          errorMessage += '<li>Invalid Non AFD Item Purchase Limit Per Month In Line # ' + index + '</li>';
        }
        if (!value[8]) {
          errorMessage += '<li>Email Id Is Required In Line # ' + index + '</li>';
        }
        else if (value[8] && !value[8].match(emailPattern)) {
          errorMessage += '<li>Invalid Non AFD Item Purchase Limit Per Month In Line # ' + index + '</li>';
        }
      }
    });

    errorMessage += '</ul>';
    return errorMessage;
  }  

  onFileChange(evt: any) {
    this.errorMessage = '';
    /* wire up file reader */
    const target: DataTransfer = evt.target as DataTransfer;
    if (this.validateUserFile(target.files)) {
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
        }
        else {
          if (!this.validateUserFileHeader(this.data)) {
            this.data = [[]];
            this.errorMessage = 'Invalid header';
          }
          else {
            this.errorMessage = this.validateUserFileValues(this.data);
            if (this.errorMessage != '<ul></ul>') {
              this.data = [[]];
            }
          }
        }
      };
      reader.readAsBinaryString(target.files[0]);
      this.isFileSelected = true;
    }

    // this.fileUpload.nativeElement.value = '';
  }

  uploadFile() {
    const file = this.files[0];
    const formData = new FormData();
    formData.append('file', this.files[0].data);
    if (formData) {
      file.inProgress = true;
      this.spinnerService.show();
      this.customerService
        .uploadCustomerData(formData)
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
            this.spinnerService.hide();
            return of(`${file.data.name} upload failed.`);
          })
        )
        .subscribe((event: any) => {
          this.spinnerService.hide();
          if (typeof event === 'object') {
            this.successStatus = event.body.statusText;
            if (this.successStatus === 'SUCCESS') {
              this.toastr.success('Uploaded Successfully!');
              this.dialogRef.close();
            }
          }
          this.spinnerService.hide();
        },
          error => {
            this.spinnerService.hide();
            this.toastr.error('Error in Uploading File!');
            this.dialogRef.close();
          });
    }
  }

  onClick() {
    const fileUpload = this.fileUpload.nativeElement;
    fileUpload.onchange = () => {
      const file = fileUpload.files[0];
      this.files.push({ data: file, inProgress: false, progress: 0, name: file.name });
    };
    fileUpload.click();
  }
}
