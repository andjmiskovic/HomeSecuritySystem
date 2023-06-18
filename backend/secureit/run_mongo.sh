#!/bin/bash
MONGO_USERNAME=admin
MONGO_PASSWORD=admin
MONGO_DATABASE=database

docker pull mongo

docker run -d -p 27017:27017 --name mongodb \
    -e MONGO_INITDB_ROOT_USERNAME=$MONGO_USERNAME \
    -e MONGO_INITDB_ROOT_PASSWORD=$MONGO_PASSWORD \
    -e MONGO_INITDB_DATABASE=$MONGO_DATABASE \
    mongo

echo "[+] MongoDB is running at localhost:27017."
echo "[i] uri: mongodb://$MONGO_USERNAME:$MONGO_PASSWORD@localhost:27017/$MONGO_DATABASE?authSource=admin
