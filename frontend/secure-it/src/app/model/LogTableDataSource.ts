import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, Observable} from "rxjs";
import {LogService} from "../services/log.service";
import {PropertyType} from "./Property";

export class LogListItem {
  id!: string;
  message!: string;
  type!: string;
  time!: string;
  source!: LogSource;
}

export enum LogType {
  INFO,
  WARNING,
  ERROR
}

export enum LogSource {
  DEVICE_MONITORING = "Device monitoring",
  DEVICE_MANAGEMENT = "Device management",
  CERTIFICATE_MANAGEMENT = "Certificate management",
  AUTHENTICATION = "Authetication",
  AUTHORIZATION = "Authorization",
  MAILING = "Mailing"
}

export function getKeyByValue(enumObject: any, value: string): string | undefined {
  for (const key in enumObject) {
    if (enumObject[key] === value) {
      return key;
    }
  }
  return undefined;
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

  loadLogs(regex: string, source: LogSource | null, sourceId: string, type: LogType | null) {
    this.loadingSubject.next(true);
    this.logService.getLogs(regex, source, sourceId, type).subscribe({
      next: (logItems) => {
        console.log(logItems)
        this.behaviorSubject.next(logItems);
        this.loadingSubject.next(false)
      },
      error: (err: any) => {
        console.log(err)
      }
    })
  }
}
