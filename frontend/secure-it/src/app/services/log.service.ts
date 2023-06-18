import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {LogListItem, LogSource, LogType} from "../model/LogTableDataSource";

@Injectable({
  providedIn: 'root'
})
export class LogService {

  private readonly logsUrl: string;

  constructor(private http: HttpClient) {
    this.logsUrl = environment.apiUrl + '/logs';
  }

  public getLogs(regex: string, source: LogSource | null, sourceId: string, type: LogType | null): Observable<LogListItem[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("regex", regex);
    if (source)
      queryParams = queryParams.append("source", source);
    queryParams = queryParams.append("sourceId", sourceId);
    if (type)
      queryParams = queryParams.append("type", type);
    return this.http.get<LogListItem[]>(this.logsUrl, AuthService.getHttpOptions(queryParams));
  }
}
