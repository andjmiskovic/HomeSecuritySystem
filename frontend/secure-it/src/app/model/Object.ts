import {User} from "./User";

export class BasicObjectDetails {
  id!: string;
  image: string = "";
  address!: string;
  type!: ObjectType;
  owner!: User;
}

export enum ObjectType {
  HOUSE,
  FLAT,
  COTTAGE,
  BUSINESS_SPACE,
  STORE
}
