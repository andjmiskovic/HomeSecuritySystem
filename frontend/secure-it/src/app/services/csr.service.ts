import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {CsrDetails} from "../model/CsrDetails";
import {CSRCreationRequest} from "../model/CSRCreationRequest";
import {CertificateCreationOptions} from "../model/CertificateCreationOptions";

@Injectable({
  providedIn: 'root'
})
export class CsrService {

  private readonly csrUrl: string;

  constructor(private http: HttpClient) {
    this.csrUrl = environment.apiUrl + '/csrs';
  }

  public getCsrs(status: string): Observable<CsrDetails[]> {
    let queryParams = new HttpParams();
    if (status !== "all")
      queryParams = queryParams.append("status", status);

    return this.http.get<CsrDetails[]>(this.csrUrl, AuthService.getHttpOptions(queryParams));
  }

  public getCsrById(id: string): Observable<CsrDetails> {
    return this.http.get<CsrDetails>(this.csrUrl + "/" + id, AuthService.getHttpOptions());
  }

  public createCsr(request: CSRCreationRequest): Observable<CsrDetails> {
    return this.http.post<CsrDetails>(this.csrUrl, request, AuthService.getHttpOptions());
  }

  public issueCertificate(id: string, options: CertificateCreationOptions): Observable<any> {
    return this.http.post<any>(this.csrUrl + "/" + id + "/certificate", options, AuthService.getHttpOptions());
  }

  public rejectRequest(id: string, reason: string): Observable<any> {
    let body = {"rejectionReason": reason};
    return this.http.post<any>(this.csrUrl + "/" + id + "/reject", body, AuthService.getHttpOptions());
  }
}
