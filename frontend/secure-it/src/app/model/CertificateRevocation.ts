export class CertificateRevocation {
  serialNumber!: BigInteger;
  revocationReason?: string;
  created!: Date;
  modified!: Date;
}
