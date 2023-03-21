export enum UserRole {
  ADMIN=1, USER=2
}

export class User {
  email!: string;
  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
  role!: string;
  blocked!: boolean;
}