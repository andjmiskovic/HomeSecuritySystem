import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {BasicPropertyDetails, PropertyType} from "../model/Property";
import {UserDetails} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private readonly objectsUrl: string;

  constructor(private http: HttpClient) {
    this.objectsUrl = environment.apiUrl + '/property';
  }

  public getProperties(search: string, type: string | undefined): Observable<BasicPropertyDetails[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    console.log(type)
    return this.http.get<BasicPropertyDetails[]>(this.objectsUrl + "/all", AuthService.getHttpOptions(queryParams));
  }

  public getPropertyTypes(): Observable<string[]> {
    return this.http.get<string[]>(this.objectsUrl + '/types', AuthService.getHttpOptions());
  }

  public getOwnersProperties(search: string, type: string | undefined, id: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    queryParams = queryParams.append("id", id);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    return this.http.get<BasicPropertyDetails[]>(this.objectsUrl + "/owner", AuthService.getHttpOptions(queryParams));
  }

  getProperty(id: string) {

  }
}
