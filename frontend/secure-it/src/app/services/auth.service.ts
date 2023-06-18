import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from "rxjs";
import {LoginResponseDto} from '../model/LoginResponseDto';
import {LoginCredentials} from '../model/LoginCredentials';
import {CreateUserCredentials, RegisterCredentials} from '../model/RegisterCredentials';
import {User} from '../model/User';
import {environment} from "../environment.development";
import {SetPasswordRequest} from "../model/SetPasswordRequest";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;

  private loggedUserSubject = new BehaviorSubject<User | undefined>(undefined);

  getNewLoggedUser(): Observable<User | undefined> {
    return this.loggedUserSubject.asObservable();
  }

  onNewUserReceived(msg: User | undefined) {
    this.loggedUserSubject.next(msg);
  }

  constructor(private http: HttpClient) {
    this.authUrl = environment.apiUrl + '/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', user, AuthService.getHttpOptions());
  }

  public register(customer: RegisterCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register', customer, AuthService.getHttpOptions());
  }

  public createUser(customer: CreateUserCredentials) {
    return this.http.post<string>(this.authUrl + '/create', customer, AuthService.getHttpOptions());
  }

  public verify(verificationCode: String): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register/verify', {code: verificationCode}, AuthService.getHttpOptions());
  }

  public logout(): Observable<void> {
    return this.http.post<void>(this.authUrl + '/logout', {}, AuthService.getHttpOptions());
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/me', AuthService.getHttpOptions());
  }

  public getIsPasswordSet(verificationCode: String): Observable<boolean> {
    return this.http.get<boolean>(this.authUrl + '/is-password-set/' + verificationCode, AuthService.getHttpOptions());
  }

  public setPassword(requestBody: SetPasswordRequest): Observable<boolean> {
    return this.http.put<boolean>(this.authUrl + '/set-password', requestBody, AuthService.getHttpOptions());
  }

  public static getHttpOptions(params: HttpParams = new HttpParams()) {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      }),
      params: params,
      withCredentials: true
    };
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('userRole') !== null;
  }

  public getRole() {
    return localStorage.getItem('userRole')
  }

}
