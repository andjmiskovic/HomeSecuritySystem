export class CertificateCreationOptions {
  extensions!: { [key: string]: any };
  issuerAlias: string = "secure it root ca";
}
