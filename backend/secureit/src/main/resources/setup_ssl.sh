#!/bin/bash

KEYSTORE="ssl_keystore.p12"

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

keytool -genkey -alias selfsigned_localhost_sslserver -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore ssl_keystore.p12 -validity 3650

if [ ! -f ssl_keystore.p12 ]; then
  echo "Exiting: Keystore was not generated."
  exit 1
fi

echo "Keystore file generated."
read -s -p "Enter the keystore password again to configure yaml: " PASSWORD

YAML="application.yml"
SEARCH="classpath:ssl_keystore.p12"

APPEND="  ssl:
    key-store-type: PKCS12
    key-store: classpath:ssl_keystore.p12
    key-store-password: $PASSWORD
    key-alias: selfsigned_localhost_sslserver"

if grep -q "$SEARCH" "$YAML"; then
  sed -i "s|key-store-password:.*|key-store-password: $PASSWORD|g" "$YAML"
else
  echo "$APPEND" >> "$YAML"
fi


echo "YAML configured. Stay safe!"
