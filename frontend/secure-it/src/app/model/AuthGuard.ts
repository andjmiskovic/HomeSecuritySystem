import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Injectable} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authenticationService: AuthService, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authenticationService.isLoggedIn()) {
      console.log(next.data["roles"])
      const userRole = this.authenticationService.getRole();
      if (next.data["roles"] && next.data["roles"].indexOf(userRole) === -1) {
        this.router.navigate(['/403']);
        return false;
      }
      return true;
    }
    this.router.navigate(['/403']);
    return false;
  }
}
