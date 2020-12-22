import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { OrdersComponent } from './admin/orders/orders.component';
import { ProductListComponent } from './customer/product-list/product-list.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { AuthGuard } from './shared/auth.guard';
import { LayoutComponent } from './admin/layout/layout.component';
import { AggregatorComponent } from './aggregator/aggregator.component';
import { AdminAuthGuard } from './shared/security/admin-auth-guard';
import { AggregatorAuthGuard } from './shared/security/aggregator-auth-guard';
import { ChangePasswordComponent} from '../app/change-password/change-password.component';
import { CustomerOrderListComponent } from '../app/customer/customer-order-list/customer-order-list.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'admin/dashboard', component: LayoutComponent, canActivate: [AdminAuthGuard] },
  { path: 'admin/orders', component: OrdersComponent, canActivate: [AdminAuthGuard] },
  { path: 'customer/product-list', component: ProductListComponent, canActivate: [AuthGuard] },
  { path: 'aggregator/orders', component: AggregatorComponent, canActivate: [AggregatorAuthGuard] },
  { path: 'changepassword', component: ChangePasswordComponent, canActivate: [AuthGuard] },
  { path: 'customer/customer-order-list', component: CustomerOrderListComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
