import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {environment} from "../environment.development";
import {BasicPropertyDetails, PropertyDetails} from "../model/Property";

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private readonly propertiesUrl: string;

  constructor(private http: HttpClient) {
    this.propertiesUrl = environment.apiUrl + '/property';
  }

  public getProperties(search: string, type: string | undefined): Observable<BasicPropertyDetails[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    console.log(type)
    return this.http.get<BasicPropertyDetails[]>(this.propertiesUrl + "/all", AuthService.getHttpOptions(queryParams));
  }

  public getPropertyTypes(): Observable<string[]> {
    return this.http.get<string[]>(this.propertiesUrl + '/types', AuthService.getHttpOptions());
  }

  getProperty(id: string): Observable<PropertyDetails> {
    return this.http.get<PropertyDetails>(this.propertiesUrl + '/' + id, AuthService.getHttpOptions());
  }

  public getUsersProperties(search: string, type: string | undefined, id: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    queryParams = queryParams.append("id", id);
    if (type != undefined)
      queryParams = queryParams.append("type", type);
    return this.http.get<BasicPropertyDetails[]>(this.propertiesUrl + "/user", AuthService.getHttpOptions(queryParams));
  }

  public getPropertiesOfOwner(email: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);
    return this.http.get<BasicPropertyDetails[]>(this.propertiesUrl + "/owner", AuthService.getHttpOptions(queryParams));
  }
}
