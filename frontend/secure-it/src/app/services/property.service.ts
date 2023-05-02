import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {BasicPropertyDetails, PropertyType} from "../model/Property";

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private readonly objectsUrl: string;

  constructor(private http: HttpClient) {
    this.objectsUrl = environment.apiUrl + '/properties';
  }

  public getObjects(search: string, type: PropertyType | undefined): Observable<BasicPropertyDetails[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    console.log(queryParams)
    return this.http.get<BasicPropertyDetails[]>(this.objectsUrl, AuthService.getHttpOptions(queryParams));
  }
}
