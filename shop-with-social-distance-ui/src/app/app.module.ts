import { BrowserModule } from '@angular/platform-browser';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { Ng2SearchPipeModule } from 'ng2-search-filter';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { HeaderComponent } from './header/header.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { OrdersComponent } from './admin/orders/orders.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {StatusPipe} from './shared/status.pipe';

import { CheckoutComponent } from './customer/checkout/checkout.component';
import { ProductListComponent } from './customer/product-list/product-list.component';
import { ToastrModule } from 'ngx-toastr';
import { NgxSpinnerModule } from 'ngx-spinner';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LayoutComponent } from './admin/layout/layout.component';
import { AggregatorComponent } from './aggregator/aggregator.component';
import { OrderDetailComponent } from './order-detail/order-detail.component';
import { CustomDatePipe } from './shared/custom-date.pipe';
import { CancelOrderDialogComponent } from './cancel-order-dialog/cancel-order-dialog.component';
import { NewCustomerComponent } from './customer/new-customer/new-customer.component';
import { InventoryUploadComponent } from '../app/admin/inventory/inventory-upload/inventory-upload.component';
import { CustomerUploadComponent } from './admin/customer/customer-upload/customer-upload.component';
import { CustomerComponent } from './admin/customer/customer.component';
import { CancelledOrdersComponent } from './aggregator/cancelled-orders/cancelled-orders.component';
import { ReadyOrdersComponent } from './aggregator/ready-orders/ready-orders.component';
import { LapsedOrdersComponent } from './aggregator/lapsed-orders/lapsed-orders.component';
import { CompletedOrdersComponent } from './aggregator/completed-orders/completed-orders.component';
import { PendingOrdersComponent } from './aggregator/pending-orders/pending-orders.component';
import { AppInterceptor } from './shared/app.interceptor';
import { InventoryComponent } from './admin/inventory/inventory.component';
import {InventoryAddupdateComponent} from '../app/admin/inventory/inventory-addupdate/inventory-addupdate.component';
import {InventoryDeleteComponent} from '../app/admin/inventory/inventory-delete/inventory-delete.component';
import { EnvServiceProvider } from './shared/env.service.provider';
import { MarkDeliveryDialogComponent } from './admin/mark-delivery-dialog/mark-delivery-dialog.component';
import {CustomerDeleteComponent} from '../app/admin/customer/customer-delete/customer-delete.component';
import { CustomerAddUpdateComponent } from '../app/admin/customer/customer-addupdate/customer-addupdate.component';
import { StoreComponent } from './admin/store/store.component';
import { AddstoreComponent } from './admin/store/addstore/addstore.component';
import { UpdateDialogComponent } from './admin/store/update-dialog/update-dialog.component';
import { UpdatestoreComponent } from './admin/store/updatestore/updatestore.component';
import { MarkInactiveStoreDialogComponent } from './admin/store/updatestore/mark-inactive-store-dialog/mark-inactive-store-dialog.component';
import { ChangePasswordComponent} from '../app/change-password/change-password.component';
import { CustomerOrderListComponent } from '../app/customer/customer-order-list/customer-order-list.component';
import { CustomerOrderDetailsComponent } from './customer/customer-order-list/customer-order-details/customer-order-details.component';
import {ForgotPasswordComponent} from '../app/login/forgot-password/forgot-password.component';
import { RxReactiveFormsModule } from '@rxweb/reactive-form-validators'
@NgModule({
  declarations: [
    AppComponent,
    StatusPipe,
    HeaderComponent,
    DashboardComponent,
    OrdersComponent,
    CheckoutComponent,
    ProductListComponent,
    LoginComponent,
    NotFoundComponent,
    LayoutComponent,
    AggregatorComponent,
    OrderDetailComponent,
    CancelOrderDialogComponent,
    CustomDatePipe,
    NewCustomerComponent,
    InventoryUploadComponent,
    CustomerUploadComponent,
    CustomerComponent,
    CancelledOrdersComponent,
    ReadyOrdersComponent,
    LapsedOrdersComponent,
    CompletedOrdersComponent,
    PendingOrdersComponent,
    InventoryComponent,
    MarkDeliveryDialogComponent,
    CustomerDeleteComponent,
    CustomerAddUpdateComponent,
    StoreComponent,
    AddstoreComponent,
    UpdateDialogComponent,
    UpdatestoreComponent,
    MarkInactiveStoreDialogComponent,
    InventoryAddupdateComponent,
    InventoryDeleteComponent,
    ChangePasswordComponent,
    CustomerOrderListComponent,
    CustomerOrderDetailsComponent,
    ForgotPasswordComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    Ng2SearchPipeModule,
    HttpClientModule,
    NgxSpinnerModule,
    RxReactiveFormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AppInterceptor,
      multi: true
    },
    EnvServiceProvider
  ],
  schemas: [
    NO_ERRORS_SCHEMA
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
