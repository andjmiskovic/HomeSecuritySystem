import {UserRole} from "./User";

export class UserTokenState {
  accessToken: string = "";
  expiresIn: number = 10000;
}

export class LoginResponseDto {
  accessToken!: string;
  expiresAt!: number;
  role!: string;
}
