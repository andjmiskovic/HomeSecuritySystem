import {User} from "./User";

export class BasicPropertyDetails {
  id!: string;
  image: string = "";
  address!: string;
  name!: string;
  type!: PropertyType;
  owner!: User;
}

export class PropertyDetails {
  id: string = "";
  image: string = "";
  address: string = "";
  name: string = "";
  type: PropertyType = PropertyType.HOUSE;
  owner: User = new User();
  tenants: User[] = [];
}

export enum PropertyType {
  HOUSE = "House",
  FLAT = "Flat",
  COTTAGE = "Cottage",
  BUSINESS_SPACE = "Business space",
  STORE = "Store"
}

export function getKeyFromValue(value: string | undefined): string | undefined {
  for (const [key, val] of Object.entries(PropertyType)) {
    if (val === value) {
      return (key as keyof typeof PropertyType);
    }
  }
  return undefined;
}
