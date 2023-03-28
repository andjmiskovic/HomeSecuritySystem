import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";

export class CertificatesListItem {
  issuedBy!: string;
  issuedTo!: string;
  validTo!: Date;
  validFrom!: Date;
}

export class CertificateTableDataSource implements DataSource<CertificatesListItem> {
  private certificatesSubject = new BehaviorSubject<CertificatesListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(
    // private certificateService: CertificateService
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<CertificatesListItem[]> {
    return this.certificatesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.certificatesSubject.complete();
    this.loadingSubject.complete();
  }

  loadCertificates(sortKind = 'start', sortDirection = 'desc', pageIndex = 0, pageSize = 10) {

    this.loadingSubject.next(true);

    // this.certificateService.getCertificates(driverEmail, customerEmail, sortKind, sortDirection,
    //   pageIndex, pageSize).pipe(
    //   catchError(() => of([])),
    //   finalize(() => this.loadingSubject.next(false))
    // )
    //   .subscribe(rides => {
    //     if ("content" in rides) {
    //       this.ridesSubject.next(rides.content);
    //     }
    //     if ("totalElements" in rides) {
    //       this.totalNumber.next(rides.totalElements);
    //     }
    //   });
  }

}


