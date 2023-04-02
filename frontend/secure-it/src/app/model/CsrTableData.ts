import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CertificateService} from "../services/certificates.service";
import {CsrService} from "../services/csr.service";
import {CertificateDetails} from "./CertificateDetails";
import {CsrDetails} from "./CsrDetails";

export class CsrListItem {
  alias!: string;
  organization!: string;
  algorithm!: string;
  keySize!: number;
  status!: string;
}

export class CsrTableDataSource implements DataSource<CsrListItem> {
  private certificatesSubject = new BehaviorSubject<CsrListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(
    private csrService: CsrService
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<CsrListItem[]> {
    return this.certificatesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.certificatesSubject.complete();
    this.loadingSubject.complete();
  }

  loadCsrs(status: string, search: string) {

    this.loadingSubject.next(true);

    this.csrService.getCsrs(status).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(csrs => {
        csrs = this.searchCsrs(search, csrs);
        let certificateItems: CsrListItem[] = []
        csrs.forEach((csr) => {
          let csrItem = new CsrListItem()
          csrItem.alias = csr.alias
          csrItem.algorithm = csr.algorithm
          csrItem.keySize = csr.keySize
          csrItem.organization = csr.organization
          csrItem.status = csr.status
          certificateItems.push(csrItem)
        })
        this.certificatesSubject.next(certificateItems)
      });
  }

  private searchCsrs(search: string, csrs: CsrDetails[]) {
    if (search !== '') {
      csrs = csrs.filter(item => {
        console.log(item)
        return Object.values(item).some(val => val !== null && val !== undefined && val.toString().includes(search));
      });
    }
    return csrs;
  }
}


