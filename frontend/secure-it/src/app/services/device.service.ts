import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CodeResponse, DeviceDetailsResponse, DeviceHandshakeData, DevicePairingInitRequest} from "../model/Device";
import {environment} from "../environment.development";
import {AuthService} from "./auth.service";
import {AlarmListItem} from "../model/AlarmsTableDataSource";

export class DeviceChangeAlarmsRequest {
  alarms: String[][];

  constructor(a: String[][]) {
    this.alarms = a;
    for (let x of this.alarms) {
      if (x.length != 4) {
        x.push("");
      }
    }
  }
}

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

  public changeAlarms(deviceId: string, body: String[][]): Observable<void> {
    return this.http.post<void>(`${this.devicesUrl}/` + deviceId, new DeviceChangeAlarmsRequest(body), AuthService.getHttpOptions());
  }

  public getAlarms(deviceId: string): Observable<AlarmListItem[]> {
    return this.http.get<AlarmListItem[]>(`${this.devicesUrl}/` + deviceId + `/alarms`, AuthService.getHttpOptions());
  }
}
