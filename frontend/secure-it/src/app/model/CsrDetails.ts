export class CsrDetails {
  id!: string;
  alias!: string;
  commonName!: string;

  country!: string;
  state!: string;
  city!: string;

  created: Date = new Date();
  modified: Date = new Date();

  algorithm!: string;
  csrPem!: string;
  publicKeyPem!: string;
  keySize!: number;

  organization!: string;
  status!: string;
}
