import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CertificateService} from "../services/certificate.service";
import {CertificateDetails} from "./CertificateDetails";

export class CertificatesListItem {
  serialNumber!: BigInteger;
  alias!: string;
  notBefore!: Date;
  notAfter!: Date;
  validityStatus!: string;
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

  loadCertificates(search: string) {

    this.loadingSubject.next(true);

    this.certificateService.getAll().pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(certificates => {
        this.certificateService.getValidities().subscribe((validities) => {
          certificates = this.searchCertificates(search, certificates);
          let certificateItems: CertificatesListItem[] = []
          certificates.forEach((certificate) => {
            let certificateItem = new CertificatesListItem()
            certificateItem.alias = certificate.alias
            certificateItem.notBefore = certificate.notBefore
            certificateItem.notAfter = certificate.notAfter
            certificateItem.serialNumber = certificate.serialNumber
            certificateItem.validityStatus = validities[Number(certificate.serialNumber)].valid ? "VALID" : "INVALID"
            certificateItems.push(certificateItem)
          })
          console.log(certificateItems)
          this.certificatesSubject.next(certificateItems)
        })
      });
  }

  private searchCertificates(search: string, certificates: CertificateDetails[]) {
    if (search !== '') {
      certificates = certificates.filter(item => {
        return Object.values(item).some(val => val.toString().includes(search));
      });
    }
    return certificates;
  }
}


