import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {CertificateDetails} from "../model/CertificateDetails";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  private readonly certificateUrl: string;

  constructor(private http: HttpClient) {
    this.certificateUrl = environment.apiUrl + '/certificates';
  }

  public getAll(sortKind = 'start', sortOrder = 'desc'): Observable<CertificateDetails[]> {
    return this.http.get<CertificateDetails[]>(this.certificateUrl, AuthService.getHttpOptions());
  }
}
