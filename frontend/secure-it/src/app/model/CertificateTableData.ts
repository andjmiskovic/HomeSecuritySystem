import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CertificatesService} from "../services/certificates.service";

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
    private certificateService: CertificatesService
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<CertificatesListItem[]> {
    return this.certificatesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.certificatesSubject.complete();
    this.loadingSubject.complete();
  }

  loadCertificates(sortKind = 'start', sortDirection = 'desc') {

    this.loadingSubject.next(true);

    this.certificateService.getCertificates(sortKind, sortDirection).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(certificates => {
        console.log(certificates)
        // if ("content" in rides) {
        //   this.certificatesSubject.next(rides.content);
        // }
        // if ("totalElements" in rides) {
        //   this.totalNumber.next(rides.totalElements);
        // }
      });
  }

}


