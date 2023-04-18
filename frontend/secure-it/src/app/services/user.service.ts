import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto} from '../model/LoginResponseDto';
import {LoginCredentials} from '../model/LoginCredentials';
import {RegisterCredentials} from '../model/RegisterCredentials';
import {User} from '../model/User';
import {environment} from "../environment.development";
import {AuthService} from "./auth.service";

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

}
