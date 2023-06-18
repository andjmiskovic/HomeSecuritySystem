#!/bin/bash

KEYSTORE="ssl_keystore.p12"
ALIAS="selfsigned_localhost_sslserver"
YAML="application.yml"
SEARCH="classpath:$KEYSTORE"
CERTIFICATE="localhost_cert.pem"

CA_KEYSTORE="../../../keystore/main.keystore"
CA_ALIAS="secure it general purpose ca (secure it root ca)"
CA_STOREPASS="example"
CA_KEYPASS="privatekeypassword"

if [ -f "$KEYSTORE" ]; then
    # Ask the user if they want to overwrite it
    read -p "Keystore already exists. Do you want to overwrite it? (y/n) " overwrite

    # Check if the user said 'yes' or 'y'
    if [ "$overwrite" = 'y' ] || [ "$overwrite" = 'yes' ]; then
        # If yes, remove the keystore
        rm "$KEYSTORE"
    else
        # If no, exit the script
        echo "Exiting."
        exit 1
    fi
fi

read -s -p "Enter the keystore password: " PASSWORD
echo ""

echo "Generating key pair and CSR"
keytool -genkeypair -alias $ALIAS -keyalg RSA -keysize 2048 \
  -dname "CN=localhost, OU=SecureIT Inc., O=SecureIT Inc., L=Novi Sad, S=Vojvodina, C=RS" \
  -ext SAN=dns:localhost,ip:127.0.0.1 -validity 3650 \
  -keystore $KEYSTORE -storepass $PASSWORD -keypass $PASSWORD

echo "Exporting the CSR"
keytool -certreq -alias $ALIAS -keystore $KEYSTORE -storepass $PASSWORD -file localhost.csr

echo "Signing the CSR with the CA private key"
keytool -gencert -infile localhost.csr -outfile localhost.crt -alias "$CA_ALIAS" -keystore $CA_KEYSTORE -storepass $CA_STOREPASS -keypass $CA_KEYPASS \
        -ext SAN=dns:localhost,ip:127.0.0.1 -rfc

echo "Importing the CA cert to the keystore"
keytool -importcert -trustcacerts -noprompt -file localhost.crt -alias $ALIAS -keystore $KEYSTORE -storepass $PASSWORD

echo "Cleaning up"
mv localhost.crt localhost_cert.cer
rm localhost.csr

echo "Exporting CA certificate (so you can import it into your web browser / device)"
keytool -exportcert -alias "$CA_ALIAS" -keystore $CA_KEYSTORE -file secureit_root_ca.cer -storepass $CA_STOREPASS

if [ ! -f $KEYSTORE ]; then
  echo "Exiting: Keystore was not generated."
  exit 1
fi

APPEND="  ssl:
    key-store-type: PKCS12
    key-store: classpath:$KEYSTORE
    key-store-password: $PASSWORD
    key-alias: $ALIAS"

if grep -q "$SEARCH" "$YAML"; then
  sed -i "s|key-store-password:.*|key-store-password: $PASSWORD|g" "$YAML"
else
  echo "$APPEND" >> "$YAML"
fi

echo "YAML configured."

openssl pkcs12 -in $KEYSTORE -passin pass:$PASSWORD -nodes -nocerts | openssl rsa -out localhost_key.pem
openssl pkcs12 -in $KEYSTORE -passin pass:$PASSWORD -nokeys -clcerts | openssl x509 -out $CERTIFICATE

echo "Certificate and key exported."

echo "All done. Stay safe!"