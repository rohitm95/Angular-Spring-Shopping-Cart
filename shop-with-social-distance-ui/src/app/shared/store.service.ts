import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EnvService } from './env.service';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { Store } from '../models/Store';

@Injectable({
  providedIn: 'root'
})
export class StoreService {

  constructor(
    private http: HttpClient,
    private env: EnvService,
    private auth: AuthService
  ) { }

  getStores(): Observable<any> {
    return this.http.get<any>(
      this.env.apiUrl + '/api/stores',
      {
        headers: this.auth.getHeaders()
      }
    );
  }

  updateStore(store: Store): Observable<any> {
    return this.http.put<any>(
      this.env.apiUrl + '/api/stores',
      store,
      {
        headers: this.auth.getHeaders()
      }
    );
  }

  addStore(store: Store): Observable<any> {
    return this.http.post<any>(
      this.env.apiUrl + '/api/stores',
      store,
      {
        headers: this.auth.getHeaders()
      }
    );
  }

  changeActiveStatus(id: number, active: boolean): Observable<any> {
    return this.http.get<any>(
      `${this.env.apiUrl}/api/stores/${id}/status?active=${active}`,
      {
        headers: this.auth.getHeaders()
      }
    );
  }
}
