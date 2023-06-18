import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, Observable} from "rxjs";
import {DeviceManagementService} from "../services/device.service";

export class AlarmListItem {
  id!: string;
  deviceId!: string;
  message!: string;
  time!: string;
}

export class AlarmsTableDataSource implements DataSource<AlarmListItem> {
  private behaviorSubject = new BehaviorSubject<AlarmListItem[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private totalNumber = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public totalNumber$ = this.totalNumber.asObservable();

  constructor(private deviceService: DeviceManagementService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<AlarmListItem[]> {
    return this.behaviorSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.behaviorSubject.complete();
    this.loadingSubject.complete();
  }

  loadAlarms(deviceId: string) {
    this.loadingSubject.next(true);
    this.deviceService.getAlarms(deviceId).subscribe({
      next: (alarms) => {
        alarms = alarms.reverse()
        this.behaviorSubject.next(alarms);
        this.loadingSubject.next(false)
      }
    })
  }
}
