import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface LoginResponse {
  user: any;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/v1/auth';
  loggedIn = new EventEmitter<boolean>();

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
      tap((response) => {
        console.log('User ID:', response.user.id);
        localStorage.setItem('access_token', response.token);
        localStorage.setItem('user_id', response.user.id);
        this.loggedIn.emit(true);
      })
    );
  }

  register(firstname: string, lastname: string, email: string, mobilenumber: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { firstname, lastname, email, mobilenumber, password });
  }
}
