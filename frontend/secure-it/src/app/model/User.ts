export enum UserRole {
  ADMIN = 1, USER = 2
}

export class User {
  id!: number;
  firstName!: string;
  lastName!: string;
  email!: string;
}

export class UserDetails {
  id!: number;
  firstName!: string;
  lastName!: string;
  email!: string;
  role!: UserRole;
  phoneNumber!: string;
  city!: string;
}
