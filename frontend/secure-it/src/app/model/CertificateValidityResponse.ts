export class CertificateValidityResponse {
  valid!: boolean;
  reason?: string;
  revokedAt: Date = new Date();
  validAfter: Date = new Date();
  validUntil: Date = new Date();
}
