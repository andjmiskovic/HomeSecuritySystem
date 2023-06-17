import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, Observable} from "rxjs";
import {LogService} from "../services/log.service";

export class LogListItem {
  id!: string;
  message!: string;
  type!: string;
  time!: string;
  source!: LogSource;
}

export enum LogSource {
  DEVICE_MONITORING = "Device monitoring",
  DEVICE_MANAGEMENT = "Device management",
  CERTIFICATE_MANAGEMENT = "Certificate management",
  AUTHENTICATION = "Authetication",
  AUTHORIZATION = "Authorization"
}

export class LogTableDataSource implements DataSource<LogListItem> {
  private behaviorSubject = new BehaviorSubject<LogListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(private logService: LogService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<LogListItem[]> {
    return this.behaviorSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.behaviorSubject.complete();
    this.loadingSubject.complete();
  }

  loadLogs(source: string, search: string, type: string) {
    this.loadingSubject.next(true);
    this.logService.getLogs(search, type, source).subscribe({
      next: (logItems) => {
        this.behaviorSubject.next(logItems);
        this.loadingSubject.next(false)
      }
    })
  }
}
