import {User} from "./User";

export class BasicPropertyDetails {
  id!: string;
  image: string = "";
  address!: string;
  name!: string;
  type!: PropertyType;
  owner!: User;
}

export enum PropertyType {
  HOUSE= "House",
  FLAT = "Flat",
  COTTAGE = "Cottage",
  BUSINESS_SPACE = "Business space",
  STORE = "Store"
}
