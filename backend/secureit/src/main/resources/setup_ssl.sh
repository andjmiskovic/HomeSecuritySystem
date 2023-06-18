#!/bin/bash

KEYSTORE="ssl_keystore.p12"
ALIAS="selfsigned_localhost_sslserver"
YAML="application.yml"
SEARCH="classpath:$KEYSTORE"
CERTIFICATE="cert.pem"

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
keytool -genkey -alias $ALIAS -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore $KEYSTORE -validity 3650 -storepass $PASSWORD -keypass $PASSWORD \
  -dname "CN=localhost, OU=SecureIT Inc., O=SecureIT Inc., L=Novi Sad, S=Serbia, C=RS" \
  -ext SAN=dns:localhost,ip:127.0.0.1

if [ ! -f $KEYSTORE ]; then
  echo "Exiting: Keystore was not generated."
  exit 1
fi

echo "Keystore file generated."

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

openssl pkcs12 -in $KEYSTORE -passin pass:$PASSWORD -nodes -nocerts | openssl rsa -out key.pem
openssl pkcs12 -in $KEYSTORE -passin pass:$PASSWORD -nokeys -clcerts | openssl x509 -out cert.pem

echo "Certificate and key exported."

echo "All done. Stay safe!"