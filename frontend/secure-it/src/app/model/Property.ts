import {User} from "./User";
import {T} from "@angular/cdk/keycodes";

export class BasicPropertyDetails {
  id!: string;
  image: string = "";
  address!: string;
  name!: string;
  type!: PropertyType;
  owner!: User;
}

export class CreatePropertyRequest {
  ownerId!: string;
  name!: string;
  address!: string;
  type!: string;
  image!: string;
}

export class UpdatePropertyRequest extends CreatePropertyRequest {
  propertyId!: string;
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

export function getValueByKey(value: string) {
  const indexOfS = Object.keys(PropertyType).indexOf(value as unknown as PropertyType);
  return Object.values(PropertyType)[indexOfS];
}
