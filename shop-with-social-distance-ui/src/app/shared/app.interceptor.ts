import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AppInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      tap(
        event => {
          // logging the http response to browser's console in case of a success
          if (event instanceof HttpResponse) {
            // console.log("api call success :", event);
          }
        },
        error => {
          // logging the http response to browser's console in case of a failuer
          if (event instanceof HttpResponse) {
            console.log('api call error :', event);
          }
        }
      )
    );
  }
}
