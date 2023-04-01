export class CertificateDetails {
  serialNumber!: number;
  alias!: string;
  subject!: string;
  issuer!: string;
  notBefore!: string;
  notAfter!: string;
  signatureAlgorithm!: string;
  version!: number;
  publicKeyAlgorithm!: number;
  publicKeyFormat!: string;
  certificateAuthority!: boolean;
}
