import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CertificateService} from "../services/certificate.service";
import {TokenService} from "../services/token.service";

export class BlacklistedTokenListItem {
  subject!: string;
  issuedAt!: string;
  expiresAt!: string;
  added!: Date;
}

export class BlacklistedTokensTableDataSource implements DataSource<BlacklistedTokenListItem> {
  private tableSubject = new BehaviorSubject<BlacklistedTokenListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(
    private tokenService: TokenService, private userRole: string
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<BlacklistedTokenListItem[]> {
    return this.tableSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.tableSubject.complete();
    this.loadingSubject.complete();
  }

  loadBlacklist() {

    this.loadingSubject.next(true);

    this.tokenService.getBlacklistedTokens().pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(blackList => {
        console.log(blackList)
        let result: BlacklistedTokenListItem[] = []
        blackList.forEach((element) => {
          if (!element.error) {
            result.push(element)
          }
        })
        this.tableSubject.next(result);
      });
  }
}
