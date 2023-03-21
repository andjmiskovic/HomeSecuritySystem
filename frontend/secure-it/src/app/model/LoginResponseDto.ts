import {UserRole} from "./User";

export class UserTokenState {
  accessToken: string = "";
  expiresIn: number = 10000;
}

export class LoginResponseDto {
  token!: UserTokenState;
  userRole: UserRole = UserRole.USER;
}
