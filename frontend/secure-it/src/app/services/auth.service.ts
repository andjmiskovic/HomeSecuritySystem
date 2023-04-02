import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto} from '../model/LoginResponseDto';
import {LoginCredentials} from '../model/LoginCredentials';
import {RegisterCredentials} from '../model/RegisterCredentials';
import {User} from '../model/User';
import {environment} from "../environment.development";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;

  constructor(private http: HttpClient) {
    this.authUrl = environment.apiUrl + '/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', user, AuthService.getHttpOptions());
  }

  public register(customer: RegisterCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register', customer, AuthService.getHttpOptions());
  }

  public verify(verificationCode: String): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register/verify', {code: verificationCode}, AuthService.getHttpOptions());
  }


  public logout(): Observable<void> {
    return this.http.post<void>(this.authUrl + '/logout/' + (localStorage.getItem('token') as string).split(" ")[1], AuthService.getHttpOptions());
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/currently-logged-user', AuthService.getHttpOptions());
  }


  public static getHttpOptions(params: HttpParams = new HttpParams()) {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
        'Content-Type': 'application/json',
      }),
      params: params
    };
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }
}
