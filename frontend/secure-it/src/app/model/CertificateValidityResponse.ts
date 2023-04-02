export class CertificateValidityResponse {
  valid!: boolean;
  reason?: string;
  revokedAt?: Date;
  validAfter?: Date;
  validUntil?: Date;
}
