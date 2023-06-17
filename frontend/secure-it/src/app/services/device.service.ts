import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CodeResponse, DeviceDetailsResponse, DeviceHandshakeData, DevicePairingInitRequest} from "../model/Device";
import {environment} from "../environment.development";
import {AuthService} from "./auth.service";
import {FormControl, ɵValue} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class DeviceManagementService {
  private readonly devicesUrl: string;

  constructor(private http: HttpClient) {
    this.devicesUrl = environment.apiUrl + '/devices';
  }

  public initializePairing(pairingRequest: DevicePairingInitRequest): Observable<CodeResponse> {
    return this.http.post<CodeResponse>(`${this.devicesUrl}/init`, pairingRequest, AuthService.getHttpOptions());
  }

  public handshakeDevice(deviceHandshakeData: DeviceHandshakeData, code: string): Observable<any> {
    return this.http.post<any>(`${this.devicesUrl}/handshake/device/${code}`, deviceHandshakeData, AuthService.getHttpOptions());
  }

  public handshakeWeb(code: string): Observable<any> {
    return this.http.post<any>(`${this.devicesUrl}/handshake/web/${code}`, {}, AuthService.getHttpOptions());
  }

  public getDevices(): Observable<DeviceDetailsResponse[]> {
    return this.http.get<DeviceDetailsResponse[]>(`${this.devicesUrl}/`, AuthService.getHttpOptions());
  }

  public getDevice(id: string | null): Observable<DeviceDetailsResponse> {
    return this.http.get<DeviceDetailsResponse>(`${this.devicesUrl}/` + id, AuthService.getHttpOptions());
  }

  public getReport(start: Date | null | undefined, end: Date | null | undefined): Observable<Blob> {
    let queryParams = new HttpParams();
    if (start && end) {
      queryParams = queryParams.append("start", start.toString());
      queryParams = queryParams.append("end", end.toString());
    }
    return this.http.get<Blob>(`${this.devicesUrl}/report`, AuthService.getHttpOptions(queryParams));
  }
}
