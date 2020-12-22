import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  setAuthToken(token: string): void {
    sessionStorage.setItem('token', token);
  }

  getAuthToken(): string {
    return sessionStorage.getItem('token');
  }

  getUserRole(): string {
    return sessionStorage.getItem('userRole');
  }

  getHeaders(): HttpHeaders {
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + this.getAuthToken())
      .set('Access-Control-Allow-Origin', '*');
  }
}
