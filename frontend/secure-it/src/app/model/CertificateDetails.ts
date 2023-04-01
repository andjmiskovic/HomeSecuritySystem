export class CertificateDetails {
  serialNumber!: number;
  alias!: string;
  subject!: string;
  issuer!: string;
  notBefore!: Date;
  notAfter!: Date;
  signatureAlgorithm!: string;
  version!: number;
  publicKeyAlgorithm!: number;
  publicKeyFormat!: string;
  certificateAuthority!: boolean;
}
