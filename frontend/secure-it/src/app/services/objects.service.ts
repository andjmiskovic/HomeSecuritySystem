import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {BasicObjectDetails, ObjectType} from "../model/Object";

@Injectable({
  providedIn: 'root'
})
export class ObjectsService {

  private readonly objectsUrl: string;

  constructor(private http: HttpClient) {
    this.objectsUrl = environment.apiUrl + '/objects';
  }

  public getObjects(search: string, type: ObjectType | undefined): Observable<BasicObjectDetails[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    return this.http.get<BasicObjectDetails[]>(this.objectsUrl, AuthService.getHttpOptions(queryParams));
  }
}
