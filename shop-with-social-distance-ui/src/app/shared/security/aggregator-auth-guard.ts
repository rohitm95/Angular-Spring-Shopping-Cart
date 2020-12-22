import { UserService } from './../user.service';
import { AuthService } from './../auth.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AggregatorAuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastr: ToastrService
  ){}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot):
  Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (
      sessionStorage.getItem('isLoggedIn') === 'true'
      && this.authService.getUserRole() === 'Aggregator'
    ){
      return true;
    } else {
      this.userService.logout();
      this.toastr.error('You must login first!');
    }
  }

}
