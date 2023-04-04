import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environment.development";
import {Observable} from "rxjs";
import {CertificateDetails} from "../model/CertificateDetails";
import {AuthService} from "./auth.service";
import {CertificateValidityResponse} from "../model/CertificateValidityResponse";
import {CertificateRevocationRequest} from "../model/CertificateRevocationRequest";
import {CertificateRevocation} from "../model/CertificateRevocation";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  private readonly certificatesUrl: string;

  constructor(private http: HttpClient) {
    this.certificatesUrl = environment.apiUrl + '/certificates';
  }

  getAll(): Observable<CertificateDetails[]> {
    return this.http.get<CertificateDetails[]>(`${this.certificatesUrl}`, AuthService.getHttpOptions());
  }

  getBySerialNumber(serialNumber: BigInteger): Observable<CertificateDetails> {
    return this.http.get<CertificateDetails>(`${this.certificatesUrl}/${serialNumber}`, AuthService.getHttpOptions());
  }

  checkCertificateValidity(serialNumber: BigInteger): Observable<CertificateValidityResponse> {
    return this.http.get<CertificateValidityResponse>(`${this.certificatesUrl}/${serialNumber}/validity`, AuthService.getHttpOptions());
  }

  revokeCertificate(serialNumber: BigInteger, request: CertificateRevocationRequest): Observable<any> {
    return this.http.post(`${this.certificatesUrl}/${serialNumber}/revoke`, request, AuthService.getHttpOptions());
  }

  findIssuerCertificates(): Observable<CertificateDetails[]> {
    return this.http.get<CertificateDetails[]>(`${this.certificatesUrl}/issuers`, AuthService.getHttpOptions());
  }

  getAllRevocations(): Observable<CertificateRevocation[]> {
    return this.http.get<CertificateRevocation[]>(`${this.certificatesUrl}/revocations`, AuthService.getHttpOptions());
  }

  getPrivateKey(serialNumber: BigInteger, password: string) {
    return this.http.post<string>(`${this.certificatesUrl}/${serialNumber}/privateKey`, {password}, AuthService.getHttpOptions());
  }

  getValidities() {
    return this.http.get<any[]>(`${this.certificatesUrl}/validities`, AuthService.getHttpOptions());
  }
}
