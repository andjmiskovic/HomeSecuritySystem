import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {CsrService} from "../services/csr.service";

export class CsrListItem {
  id!: string;
  alias!: string;
  commonName!: string;
  organization!: string;
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

  loadCsrs(status: string) {
    this.loadingSubject.next(true);
    this.csrService.getCsrs(status).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(csrs => {
        let certificateItems: CsrListItem[] = []
        csrs.forEach((csr) => {
          let csrItem = new CsrListItem();
          csrItem.id = csr.id;
          csrItem.commonName = csr.commonName;
          csrItem.alias = csr.alias;
          csrItem.organization = csr.organization;
          csrItem.status = csr.status;
          certificateItems.push(csrItem);
        })
        this.certificatesSubject.next(certificateItems);
      });
  }

}


