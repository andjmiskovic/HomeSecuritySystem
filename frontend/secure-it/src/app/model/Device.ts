import {PropertyResponse} from "./Property";

export class DevicePairingInitRequest {
  propertyId!: string;
}

export class Sensor {
  name!: string;
  unit!: string;
  type!: string;
}

export class DeviceHandshakeData {
  name!: string;
  label!: string;
  type!: string;
  manufacturer!: string;
  macAddress!: string;
  publicKey!: string;
  sensors?: Sensor[]
}

export class DeviceDetailsResponse {
  id!: string;
  name!: string;
  type!: string;
  manufacturer!: string;
  macAddress!: string;
  label!: string;
  property!: PropertyResponse;
  sensors: Sensor[] = [];
  alarms: String[][] = [];
}

export class CodeResponse {
  code!: string;
}
