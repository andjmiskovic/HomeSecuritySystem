import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {environment} from "../environment.development";
import {AuthService} from "./auth.service";
import {BlacklistedTokenInfo} from "../model/BlacklistedTokenInfo";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly tokenUrl: string;

  constructor(private http: HttpClient) {
    this.tokenUrl = environment.apiUrl + '/tokens';
  }

  public getBlacklistedTokens(): Observable<BlacklistedTokenInfo[]> {
    return this.http.get<BlacklistedTokenInfo[]>(this.tokenUrl, AuthService.getHttpOptions());
  }

}
