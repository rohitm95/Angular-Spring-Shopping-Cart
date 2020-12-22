import { HttpErrorResponse } from '@angular/common/http';
import { StoreService } from './../../../shared/store.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { StoreComponent } from '../store.component';
import { MatDialog } from '@angular/material/dialog';
import { InventoryUploadComponent } from '../../inventory/inventory-upload/inventory-upload.component';

import { AddstoreComponent} from '../addstore/addstore.component';
import { UpdateDialogComponent } from '../update-dialog/update-dialog.component';
import { Store } from 'src/app/models/Store';
import { ToastrService } from 'ngx-toastr';
import { MarkInactiveStoreDialogComponent } from './mark-inactive-store-dialog/mark-inactive-store-dialog.component';

@Component({
  selector: 'app-updatestore',
  templateUrl: './updatestore.component.html',
  styleUrls: ['./updatestore.component.scss']
})
export class UpdatestoreComponent implements OnInit {

  displayedColumns: string[] = ['store_id', 'store_name', 'slotDuration', 'deliveryInSlot', 'actions'];
  dataSource = new MatTableDataSource();
  ELEMENT_DATA = [];
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private dialog: MatDialog,
    private storeService: StoreService,
    private toastr: ToastrService
  ) { }

  openDialog(store: Store){
    this.dialog.open(
      UpdateDialogComponent,
      {
        height: '90vh',
        width: '100vw',
        data: {
          store
        }
      })
    .afterClosed().subscribe(
      (res: any) => {
        console.log(print);
      }
    );
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.getStores();
  }

  getStores(): void {
    this.storeService.getStores().subscribe(
      (response: any) => {
        if (response.statusText === 'SUCCESS') {
          let stores: Array<Store> = response.result;
          stores.forEach(store => {
            store.slotDuration = store.slotDuration.toString();
            store.storeHolidays.forEach(holiday => {
              holiday.date = new Date(holiday.date);
            });
          });
          this.dataSource.data = stores;
        } else {
          this.toastr.error(response.statusText);
        }
      },
      (error: any) => {
        this.toastr.error('Connection Error!');
      }
    );
  }

  askForConfirmation(store: Store): void {
    const dialogRef = this.dialog.open(MarkInactiveStoreDialogComponent, {
      width: '44%',
      data: store
    }).afterClosed().subscribe(res => {

    });
  }
}