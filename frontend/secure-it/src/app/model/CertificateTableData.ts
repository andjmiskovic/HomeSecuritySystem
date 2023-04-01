import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CertificateService} from "../services/certificates.service";

export class CertificatesListItem {
  serialNumber!: number;
  alias!: string;
  notBefore!: string;
  notAfter!: string;
}

export class CertificateTableDataSource implements DataSource<CertificatesListItem> {
  private certificatesSubject = new BehaviorSubject<CertificatesListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(
    private certificateService: CertificateService
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<CertificatesListItem[]> {
    return this.certificatesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.certificatesSubject.complete();
    this.loadingSubject.complete();
  }

  loadCertificates() {

    this.loadingSubject.next(true);

    this.certificateService.getAll().pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(certificates => {
        console.log("AAAAA")
        console.log(certificates)
        let certificateItems: CertificatesListItem[] = []
        certificates.forEach((certificate) => {
          let certificateItem = new CertificatesListItem()
          certificateItem.alias = certificate.alias
          certificateItem.notBefore = certificate.notBefore
          certificateItem.notAfter = certificate.notAfter
          certificateItem.serialNumber = certificate.serialNumber
          certificateItems.push(certificateItem)
        })
        this.certificatesSubject.next(certificateItems)
        // if ("content" in rides) {
        //   this.certificatesSubject.next(rides.content);
        // }
        // if ("totalElements" in rides) {
        //   this.totalNumber.next(rides.totalElements);
        // }
      });
  }

}


