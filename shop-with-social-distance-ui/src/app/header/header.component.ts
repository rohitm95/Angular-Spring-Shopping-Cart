import { Component, OnInit } from '@angular/core';
import { UserService } from '../shared/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userName;
  userRole;
  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.getUsername();    
    this.userRole = window.sessionStorage.getItem('userRole');
  }

  logOff() {
    this.userService.logout();
  }

  getUsername() {
    this.userName = sessionStorage.getItem('userName');
  }

  navigateTo(): void {
    const role = sessionStorage.getItem('userRole');
    switch (role.toLowerCase()) {
      case 'admin':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'customer':
        this.router.navigate(['/customer/product-list']);
        break;
      case 'aggregator':
        this.router.navigate(['/aggregator/orders']);
        break;
    } 
  }
}