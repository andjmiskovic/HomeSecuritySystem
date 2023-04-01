export class CertificateDetails {
  serialNumber!: BigInteger;
  alias!: string;
  subject!: string;
  issuer!: string;
  notBefore!: Date;
  notAfter!: Date;
  signatureAlgorithm!: string;
  version!: number;
  publicKeyAlgorithm!: string;
  certificateAuthority!: boolean;
}
