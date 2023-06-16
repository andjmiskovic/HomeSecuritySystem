import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CodeResponse, DeviceDetailsResponse, DeviceHandshakeData, DevicePairingInitRequest} from "../model/Device";
import {environment} from "../environment.development";

@Injectable({
  providedIn: 'root'
})
export class DeviceManagementService {
  private readonly devicesUrl: string;

  constructor(private http: HttpClient) {
    this.devicesUrl = environment.apiUrl + '/devices';
  }

  public initializePairing(pairingRequest: DevicePairingInitRequest): Observable<CodeResponse> {
    return this.http.post<CodeResponse>(`${this.devicesUrl}/init`, pairingRequest);
  }

  public handshakeDevice(deviceHandshakeData: DeviceHandshakeData, code: string): Observable<any> {
    return this.http.post<any>(`${this.devicesUrl}/handshake/device/${code}`, deviceHandshakeData);
  }

  public handshakeWeb(code: string): Observable<any> {
    return this.http.post<any>(`${this.devicesUrl}/handshake/web/${code}`, {});
  }

  public getDevices(): Observable<DeviceDetailsResponse[]> {
    return this.http.get<DeviceDetailsResponse[]>(`${this.devicesUrl}/`);
  }

  public getDevice(id: string | null): Observable<DeviceDetailsResponse> {
    return this.http.get<DeviceDetailsResponse>(`${this.devicesUrl}/` + id);
  }
}
