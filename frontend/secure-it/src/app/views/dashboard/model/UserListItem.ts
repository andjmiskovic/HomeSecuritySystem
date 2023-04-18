export class UserListItem {
  email!: string;
  name!: string;
  type!: string;

  constructor(email: string, name: string, type: string) {
    this.email = email;
    this.name = name;
    this.type = type;
  }
}
