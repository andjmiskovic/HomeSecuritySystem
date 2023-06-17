import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {LogListItem} from "../model/LogTableDataSource";

@Injectable({
  providedIn: 'root'
})
export class LogService {

  private readonly logsUrl: string;

  constructor(private http: HttpClient) {
    this.logsUrl = environment.apiUrl + '/logs';
  }

  public getLogs(search: string, type: string | undefined, source: string): Observable<LogListItem[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    queryParams = queryParams.append("source", source);
    return this.http.get<LogListItem[]>(this.logsUrl + "/all", AuthService.getHttpOptions(queryParams));
  }
}
