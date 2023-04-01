import {CountryCode} from './CountryCode';
import {IsIn, IsNotEmpty, IsPositive} from 'class-validator';

export class CSRCreationRequest {
  @IsNotEmpty({message: 'Organization name cannot be blank'})
  organization!: string;

  @IsNotEmpty({message: 'City cannot be blank'})
  city!: string;

  @IsNotEmpty({message: 'State cannot be blank'})
  state!: string;

  @IsNotEmpty({message: 'Country code cannot be blank'})
  @CountryCode()
  country!: string;

  @IsNotEmpty({message: 'Algorithm cannot be blank'})
  @IsIn(['RSA', 'DSA', 'EC'], {message: 'Invalid algorithm. Allowed algorithms are RSA, DSA and EC.'})
  algorithm!: string;

  @IsPositive({message: 'Keysize must be a positive integer.'})
  keySize!: number;

  constructor(partial: Partial<CSRCreationRequest>) {
    Object.assign(this, partial);
  }
}
