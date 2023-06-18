import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {User, UserDetails} from '../model/User';
import {environment} from "../environment.development";
import {AuthService} from "./auth.service";
import {CreateUserCredentials} from "../model/RegisterCredentials";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string;

  constructor(private http: HttpClient) {
    this.userUrl = environment.apiUrl + '/user';
  }

  public getPropertyOwners(): Observable<User[]> {
    return this.http.get<User[]>(this.userUrl, AuthService.getHttpOptions());
  }

  public getPropertyOwner(userEmail: string): Observable<UserDetails> {
    return this.http.get<UserDetails>(this.userUrl + '/' + userEmail, AuthService.getHttpOptions());
  }

  public deleteUser(email: string): Observable<boolean> {
    return this.http.delete<boolean>(this.userUrl + "/" + email, AuthService.getHttpOptions())
  }

  public editPropertyOwner(requestBody: CreateUserCredentials) {
    return this.http.put<string>(this.userUrl, requestBody, AuthService.getHttpOptions());
  }


}
